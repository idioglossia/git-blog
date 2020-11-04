package lab.idioglossia.gitblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {
    private final Environment environment;

    @Autowired
    public HomeController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/")
    public ModelAndView home() {
        List<String> ap = Arrays.asList(environment.getActiveProfiles());
        if (ap.contains("initialized"))
            return new ModelAndView("redirect:/panel");
        return new ModelAndView("redirect:/init");
    }

}
