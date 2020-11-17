package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.dto.PostDto;
import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.model.entity.TagEntity;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.PostRepository;
import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.HistoryEntityFactoryService;
import lab.idioglossia.gitblog.service.IndexesService;
import lab.idioglossia.gitblog.util.UserAuthHelper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Profile("initialized")
public class PostService {
    private final UserService userService;
    private final TagsService tagsService;
    private final PostRepository postRepository;
    private final HistoryEntityFactoryService historyEntityFactoryService;
    private final HistoryRepository historyRepository;
    private final IndexesService indexesService;
    private final FileService fileService;
    private final GitService gitService;

    public PostService(UserService userService, TagsService tagsService, PostRepository postRepository, HistoryEntityFactoryService historyEntityFactoryService, HistoryRepository historyRepository, IndexesService indexesService, FileService fileService, GitService gitService) {
        this.userService = userService;
        this.tagsService = tagsService;
        this.postRepository = postRepository;
        this.historyEntityFactoryService = historyEntityFactoryService;
        this.historyRepository = historyRepository;
        this.indexesService = indexesService;
        this.fileService = fileService;
        this.gitService = gitService;
    }

    public PostEntity getPost(Integer id){
        return postRepository.get(id);
    }

    public List<PostEntity> getPosts(UserEntity userEntity, int page, int pageSize){
        int from = page * pageSize;
        int to = (page + 1) * pageSize;

        if(userEntity.getPostIds() == null)
            return new ArrayList<>();

        List<Integer> keys = userEntity.getPostIds();
        Collections.reverse(keys);

        List<PostEntity> postEntities = new ArrayList<>();
        for(int i = from; i < to && i < keys.size(); i++){
            PostEntity postEntity = postRepository.get(keys.get(i));
            if(postEntity != null)
                postEntities.add(postEntity);
        }

        return postEntities;
    }

    public List<PostEntity> getPosts(int page, int pageSize){
        int from = page * pageSize;
        int to = (page + 1) * pageSize;

        List<Integer> keys = postRepository.keys();
        keys.sort(Integer::compareTo);
        Collections.reverse(keys);

        List<PostEntity> postEntities = new ArrayList<>();
        for(int i = from; i < to && i < keys.size(); i++){
            PostEntity postEntity = postRepository.get(keys.get(i));
            if(postEntity != null)
                postEntities.add(postEntity);
        }

        return postEntities;
    }

    public boolean deletePost(int id){
        PostEntity postEntity = postRepository.get(id);
        if(postEntity == null)
            return false;

        if(!UserAuthHelper.isCurrentUserAdmin() && !postEntity.getUsername().equals(userService.getCurrentUsername()))
            return false;

        fileService.removeImage(postEntity.getCover());
        userService.editUser(postEntity.getUsername(), new UserService.UserEditor() {
            @Override
            public void editUser(UserEntity userEntity) {
                userEntity.getPostIds().remove(id);
            }
        });
        postEntity.getTags().forEach(tag -> {
            tagsService.editTag(tag, new TagsService.TagEditor() {
                @Override
                public void edit(TagEntity tagEntity) {
                    tagEntity.getPostIds().remove(id);
                }
            });
        });
        indexesService.removePost(postEntity.getId());
        postRepository.delete(postEntity);
        historyRepository.save(historyEntityFactoryService.postDeleted(userService.getCurrentUsername(), postEntity));
        gitService.addAndCommit("Removed a post from " + postEntity.getUsername());
        return true;
    }

    public PostEntity addPost(PostDto postDto){
        UserEntity userEntity = userService.getCurrentUser();

        PostEntity postEntity = PostEntity.builder()
                .content(postDto.getContent())
                .date(new Date())
                .description(postDto.getDescription())
                .title(postDto.getTitle())
                .username(userEntity.getUsername())
                .followUpButton(getFollowUpButton(postDto))
                .tags(getTags(postDto))
                .build();

        handleCover(postDto, postEntity);
        handleThumbnail(postDto, postEntity);
        postRepository.save(postEntity);

        indexesService.addPost(postEntity.getId());
        postEntity.getTags().forEach(tag -> {
            tagsService.editTag(tag, tagEntity -> {
                tagEntity.getPostIds().add(postEntity.getId());
            });
        });
        userService.editUser(userEntity.getUsername(), userEntity1 -> {
            if (userEntity1.getPostIds() == null) {
                userEntity1.setPostIds(new ArrayList<>());
            }
            userEntity1.getPostIds().add(postEntity.getId());
        });

        historyRepository.save(historyEntityFactoryService.postAdded(userEntity, postEntity));
        gitService.addAndCommit("Added new post by " + userEntity.getUsername());

        return postEntity;
    }

    public PostEntity editPost(Integer id, PostDto postDto) {
        PostEntity postEntity = postRepository.get(id);
        if(postEntity == null || (!postEntity.getUsername().equals(userService.getCurrentUsername()) && !UserAuthHelper.isCurrentUserAdmin()))
            return null;

        UserEntity userEntity = userService.getUser(postEntity.getUsername());

        handleCover(postDto, postEntity);
        handleThumbnail(postDto, postEntity);
        postEntity.setContent(postDto.getContent());
        postEntity.setDescription(postDto.getDescription());
        postEntity.setTitle(postDto.getTitle());
        postEntity.setFollowUpButton(getFollowUpButton(postDto));

        List<String> tags = getTags(postDto);
        List<String> removedTags = new ArrayList<>(postEntity.getTags());
        removedTags.removeAll(tags);
        removedTags.forEach(tag -> {
            tagsService.editTag(tag, new TagsService.TagEditor() {
                @Override
                public void edit(TagEntity tagEntity) {
                    tagEntity.getPostIds().remove(id);
                }
            });
        });

        List<String> newTags = new ArrayList<>(tags);
        newTags.removeAll(postEntity.getTags());
        newTags.forEach(tag -> {
            tagsService.editTag(tag, tagEntity -> {
                tagEntity.getPostIds().add(postEntity.getId());
            });
        });
        postEntity.setTags(tags);

        postRepository.update(postEntity);
        historyRepository.save(historyEntityFactoryService.postUpdated(userEntity, postEntity));
        gitService.addAndCommit("Post updated by" + userEntity.getUsername());

        return postEntity;
    }

    private void handleCover(PostDto postDto, PostEntity postEntity) {
        if(postDto.isRemoveCover()){
            if (postEntity.getCover() != null) {
                fileService.removeImage(postEntity.getCover());
            }
            postEntity.setCover(null);
            return;
        }

        if (postDto.getCoverImage() != null && !StringUtils.isEmpty(postDto.getCoverImage().getOriginalFilename())) {
            if (postEntity.getCover() != null) {
                fileService.removeImage(postEntity.getCover());
            }
            postEntity.setCover(fileService.createImage(postDto.getCoverImage()));
        }
    }

    private void handleThumbnail(PostDto postDto, PostEntity postEntity) {
        if(postDto.isRemoveThumbnail()){
            if (postEntity.getThumbnail() != null) {
                fileService.removeImage(postEntity.getThumbnail());
            }
            postEntity.setThumbnail(null);
            return;
        }

        if (postDto.getThumbnailImage() != null && !StringUtils.isEmpty(postDto.getThumbnailImage().getOriginalFilename())) {
            if (postEntity.getThumbnail() != null) {
                fileService.removeImage(postEntity.getThumbnail());
            }
            postEntity.setThumbnail(fileService.createImage(postDto.getThumbnailImage()));
        }
    }


    private List<String> getTags(PostDto postDto) {
        if(StringUtils.isEmpty(postDto.getTags()))
            return new ArrayList<>();
        String[] splitTags = postDto.getTags().split(",");
        List<String> tags = new ArrayList<>();
        List<String> keys = tagsService.getKeys();
        for (String splitTag : splitTags) {
            splitTag = splitTag.trim();
            while (splitTag.startsWith(" ")){
                splitTag = splitTag.substring(1);
            }
            if(keys.contains(splitTag.toLowerCase())){
                tags.add(splitTag.toLowerCase());
            }
        }
        return tags;
    }

    private PostEntity.FollowUpButton getFollowUpButton(PostDto postDto) {
        if(!StringUtils.isEmpty(postDto.getFollowUpLink()) && !StringUtils.isEmpty(postDto.getFollowUpText())){
            return PostEntity.FollowUpButton.builder()
                    .link(postDto.getFollowUpLink())
                    .text(postDto.getFollowUpText())
                    .build();
        }
        return null;
    }
}
