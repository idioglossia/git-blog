package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.service.GitService;
import lab.idioglossia.gitblog.service.panel.PanelHomeService;
import lab.idioglossia.gitblog.service.panel.TagsService;
import lab.idioglossia.gitblog.service.panel.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@Profile("initialized")
public class PanelController extends AbstractPanelController {
    private final PanelHomeService panelHomeService;
    private final TagsService tagsService;
    private final GitService gitService;

    public PanelController(PanelHomeService panelHomeService, UserService userService, TagsService tagsService, GitService gitService) {
        super(userService);
        this.panelHomeService = panelHomeService;
        this.tagsService = tagsService;
        this.gitService = gitService;
    }

    @GetMapping("/panel")
    public ModelAndView home() {
        Map<String, Object> model = getBaseModel("Home");
        model.put("today", panelHomeService.getTodayDateString());
        model.put("reposize", panelHomeService.getRepositorySizes());
        model.put("histories", panelHomeService.getHistories());
        return new ModelAndView("panel", model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/push")
    public ModelAndView push(){
        gitService.push();
        return home();
    }

    @GetMapping("/panel/tags")
    public ModelAndView tagsListPage(){
        Map<String, Object> model = getBaseModel("Tags");
        model.put("tags", tagsService.getAllTags());
        return new ModelAndView("tags", model);
    }

    @PostMapping("/panel/tags")
    public ModelAndView addTag(@RequestParam String tag){
        tagsService.create(tag);
        return tagsListPage();
    }
}
