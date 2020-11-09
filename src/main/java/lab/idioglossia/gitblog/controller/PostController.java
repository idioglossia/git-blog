package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.PostDto;
import lab.idioglossia.gitblog.model.entity.PostEntity;
import lab.idioglossia.gitblog.service.panel.PostService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class PostController extends AbstractPanelController{
    private final PostService postService;

    @Autowired
    public PostController(UserService userService, PostService postService) {
        super(userService);
        this.postService = postService;
    }

    @GetMapping("/panel/posts")
    public ModelAndView getPosts(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Map<String, Object> model = getBaseModel("Posts");
        List<PostEntity> posts = postService.getPosts(page, pageSize);
        model.put("posts", posts);
        model.put("page", page);
        return new ModelAndView("posts", model);
    }

    @GetMapping("/panel/posts/new")
    public ModelAndView addNewPostPage(){
        Map<String, Object> model = getBaseModel("Add Post");
        return new ModelAndView("post_add", model);
    }

    @PostMapping("/panel/posts/new")
    public ModelAndView addNewPost(@Valid PostDto postDto){
        PostEntity postEntity = postService.addPost(postDto);
        return new ModelAndView("redirect:/panel/posts/"+postEntity.getId());
    }
}
