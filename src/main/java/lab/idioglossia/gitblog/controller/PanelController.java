package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.service.panel.PanelHomeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@Profile("initialized")
public class PanelController {
    private final PanelHomeService panelHomeService;

    public PanelController(PanelHomeService panelHomeService) {
        this.panelHomeService = panelHomeService;
    }

    @GetMapping("/panel")
    public ModelAndView home() {
        Map<String, Object> model = new HashMap<>();
        model.put("today", panelHomeService.getTodayDateString());
        model.put("reposize", panelHomeService.getRepositorySizes());
        model.put("histories", panelHomeService.getHistories());
        return new ModelAndView("panel", model);
    }
}
