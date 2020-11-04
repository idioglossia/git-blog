package lab.idioglossia.gitblog.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Profile("initialized")
public class PanelController {

    @GetMapping("/panel")
    public ModelAndView panel() {
        return new ModelAndView("panel");
    }
}
