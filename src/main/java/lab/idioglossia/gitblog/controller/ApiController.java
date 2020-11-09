package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.ApiCallResult;
import lab.idioglossia.gitblog.service.panel.TagsService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserService userService;
    private final TagsService tagsService;

    @Autowired
    public ApiController(UserService userService, TagsService tagsService) {
        this.userService = userService;
        this.tagsService = tagsService;
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

}
