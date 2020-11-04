package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lab.idioglossia.gitblog.service.InitializerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class InitializeController {
    private final InitializerService initializerService;
    private final boolean initAlready;

    public InitializeController(InitializerService initializerService, @Value("${gitblog.config.init}") String initValue) {
        this.initializerService = initializerService;
        initAlready = initValue.equals("true");
    }

    @GetMapping("/init")
    public ModelAndView view(){
        if(initAlready)
            return new ModelAndView("redirect:/panel");
        return new ModelAndView("initialize");
    }

    @PostMapping("/init")
    public ModelAndView initialize(@Valid InitializeDto initializeDto){
        if(initAlready)
            return new ModelAndView("redirect:/panel");
        initializerService.initialize(initializeDto);
        return new ModelAndView("restart");
    }

}
