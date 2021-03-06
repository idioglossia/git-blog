package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.UserAddDto;
import lab.idioglossia.gitblog.model.dto.UserEditDto;
import lab.idioglossia.gitblog.model.entity.UserEntity;
import lab.idioglossia.gitblog.service.panel.PostService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Profile("initialized")
public class UserController extends AbstractPanelController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        super(userService);
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/panel/users")
    public ModelAndView users(){
        Map<String, Object> model = getBaseModel("Users");
        model.put("users", userService.getUsersList());
        return new ModelAndView("users", model);
    }

    @GetMapping("/panel/users/{username}")
    public ModelAndView user(@PathVariable(value = "username") String username, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Map<String, Object> model = getBaseModel("User (" + username + ")");
        UserEntity userEntity = userService.getUser(username);
        model.put("user", userEntity);
        model.put("posts", postService.getPosts(userEntity, page, pageSize));

        return new ModelAndView("user_view", model);
    }

    @GetMapping("/panel/users/edit/{username}")
    public ModelAndView getEditUser(@PathVariable(value = "username") String username){
        Map<String, Object> model = getBaseModel("Edit User (" + username + ")");
        model.put("user", userService.getUser(username));
        return new ModelAndView("user_edit", model);
    }

    @PostMapping("/panel/users/edit/{username}")
    public ModelAndView doEditUser(@PathVariable(value = "username") String username, UserEditDto userEditDto){
        userService.editUser(username, userEditDto);
        return getEditUser(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/panel/users/new")
    public ModelAndView getAddNewUser(boolean failed){
        Map<String, Object> model = getBaseModel("Add User");
        if(failed){
            System.out.println("Failed!");
            model.put("fail", true);
        }
        return new ModelAndView("user_add", model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/panel/users/new")
    public ModelAndView addNewUser(@Valid UserAddDto userAddDto){
        boolean added = userService.addUser(userAddDto);
        if(added){
            return new ModelAndView("redirect:/panel/users/"+userAddDto.getUsername());
        }else {
            return getAddNewUser(true);
        }
    }
}
