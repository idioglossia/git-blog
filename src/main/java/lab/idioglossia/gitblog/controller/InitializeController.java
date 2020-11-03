package lab.idioglossia.gitblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InitializeController {

    @GetMapping("/init")
    public ModelAndView modelAndView(){
        return new ModelAndView("initialize");
    }

}
