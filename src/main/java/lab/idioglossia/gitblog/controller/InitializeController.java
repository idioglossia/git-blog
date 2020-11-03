package lab.idioglossia.gitblog.controller;

import lab.idioglossia.gitblog.model.dto.InitializeDto;
import lab.idioglossia.gitblog.service.InitializerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class InitializeController {
    private final InitializerService initializerService;

    public InitializeController(InitializerService initializerService) {
        this.initializerService = initializerService;
    }

    @GetMapping("/init")
    public ModelAndView view(){
        return new ModelAndView("initialize");
    }

    @PostMapping("/init")
    public ModelAndView initialize(@Valid InitializeDto initializeDto){
        initializerService.initialize(initializeDto);
        return new ModelAndView("initialize");
    }

}
