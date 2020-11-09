package lab.idioglossia.gitblog.service.panel;

import lab.idioglossia.gitblog.model.PostPreview;
import lab.idioglossia.gitblog.model.UserPreview;
import lab.idioglossia.gitblog.model.dto.PostDto;
import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.model.entity.TagEntity;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.repository.FileRepository;
import lab.idioglossia.gitblog.repository.HistoryRepository;
import lab.idioglossia.gitblog.repository.PostRepository;
import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.HistoryEntityFactoryService;
import lab.idioglossia.gitblog.util.UserAuthHelper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PostService {
    private final UserService userService;
    private final TagsService tagsService;
    private final PostRepository postRepository;
    private final HistoryEntityFactoryService historyEntityFactoryService;
    private final HistoryRepository historyRepository;
    private final FileRepository fileRepository;
    private final GitService gitService;

    public PostService(UserService userService, TagsService tagsService, PostRepository postRepository, HistoryEntityFactoryService historyEntityFactoryService, HistoryRepository historyRepository, FileRepository fileRepository, GitService gitService) {
        this.userService = userService;
        this.tagsService = tagsService;
        this.postRepository = postRepository;
        this.historyEntityFactoryService = historyEntityFactoryService;
        this.historyRepository = historyRepository;
        this.fileRepository = fileRepository;
        this.gitService = gitService;
    }

    public List<PostEntity> getPosts(UserEntity userEntity, int page, int pageSize){
        int from = page * pageSize;
        int to = (page + 1) * pageSize;

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

        fileRepository.removeFile("images", postEntity.getCover());
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
                    for (PostPreview postPreview : tagEntity.getPosts()) {
                        if(postPreview.getId() == id){
                            tagEntity.getPosts().remove(postPreview);
                            break;
                        }
                    }
                }
            });
        });
        postRepository.delete(postEntity);

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
        postRepository.save(postEntity);


        postEntity.getTags().forEach(tag -> {
            tagsService.editTag(tag, tagEntity -> {
                tagEntity.getPostIds().add(postEntity.getId());
                tagEntity.getPosts().add(PostPreview.builder()
                        .title(postEntity.getTitle())
                        .user(UserPreview.from(userEntity))
                        .description(postEntity.getDescription())
                        .build());
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

    private void handleCover(PostDto postDto, PostEntity postEntity) {
        if(postDto.isRemoveCover()){
            postEntity.setCover(null);
            return;
        }

        if (postDto.getCoverImage() != null && !StringUtils.isEmpty(postDto.getCoverImage().getOriginalFilename())) {

            if (postEntity.getCover() != null) {
                fileRepository.removeFile("images", postEntity.getCover());
            }

            String profilePicName = UUID.randomUUID().toString();
            String extension = FilenameUtils.getExtension(postDto.getCoverImage().getOriginalFilename());
            String fullPP = profilePicName + "." + extension;
            fileRepository.addFile("images", fullPP, postDto.getCoverImage());
            postEntity.setCover(fullPP);
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
            if(keys.contains(splitTag)){
                tags.add(splitTag);
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
