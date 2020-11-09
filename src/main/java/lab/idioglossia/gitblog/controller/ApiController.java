package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.ApiCallResult;
import lab.idioglossia.gitblog.service.panel.PostService;
import lab.idioglossia.gitblog.service.panel.TagsService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;
    private final TagsService tagsService;
    private final PostService postService;

    @Autowired
    public ApiController(UserService userService, TagsService tagsService, PostService postService) {
        this.userService = userService;
        this.tagsService = tagsService;
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{username}")
    public ApiCallResult deleteUser(@PathVariable String username){
        return new ApiCallResult(userService.deleteUser(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tags/{tag}")
    public ApiCallResult deleteTag(@PathVariable String tag){
        return new ApiCallResult(tagsService.delete(tag));
    }

    @DeleteMapping("/posts/{postId}")
    public ApiCallResult deletePost(@PathVariable Integer postId){
        return new ApiCallResult(postService.deletePost(postId));
    }

    @GetMapping("/tags/names")
    public List<String> getTagNames(){
        return tagsService.getKeys();
    }

}
