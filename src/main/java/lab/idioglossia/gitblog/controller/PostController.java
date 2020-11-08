package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class PostController extends AbstractPanelController{

    @Autowired
    public PostController(UserService userService) {
        super(userService);
    }

    @GetMapping("/panel/posts/new")
    public ModelAndView addNewPostPage(){
        Map<String, Object> model = getBaseModel("Add Post");
        return new ModelAndView("post_add", model);
    }
}
