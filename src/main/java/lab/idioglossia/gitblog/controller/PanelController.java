package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.service.panel.PanelHomeService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@Profile("initialized")
public class PanelController {
    private final PanelHomeService panelHomeService;
    private final UserService userService;

    public PanelController(PanelHomeService panelHomeService, UserService userService) {
        this.panelHomeService = panelHomeService;
        this.userService = userService;
    }

    @GetMapping("/panel")
    public ModelAndView home() {
        Map<String, Object> model = getBaseModel("Home");
        model.put("today", panelHomeService.getTodayDateString());
        model.put("reposize", panelHomeService.getRepositorySizes());
        model.put("histories", panelHomeService.getHistories());
        return new ModelAndView("panel", model);
    }

    @GetMapping("/panel/users")
    public ModelAndView users(){
        Map<String, Object> model = getBaseModel("Users");
        model.put("users", userService.getUsersList());
        return new ModelAndView("users", model);
    }

    @GetMapping("/panel/users/{username}")
    public ModelAndView user(@PathVariable(value = "username") String username){
        Map<String, Object> model = getBaseModel("User (" + username + ")");
        model.put("user", userService.getUser(username));
        return new ModelAndView("user_view", model);
    }


    private Map<String, Object> getBaseModel(String page){
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", userService.getCurrentUser());
        model.put("currentPage", page);
        return model;
    }
}
