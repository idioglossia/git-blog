package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.service.panel.UserService;

import java.util.HashMap;
import java.util.Map;

public class AbstractPanelController {
    private final UserService userService;

    public AbstractPanelController(UserService userService) {
        this.userService = userService;
    }

    protected Map<String, Object> getBaseModel(String page){
        Map<String, Object> model = new HashMap<>();
        model.put("currentUser", userService.getCurrentUser());
        model.put("currentPage", page);
        return model;
    }

}
