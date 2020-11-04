package lab.idioglossia.gitblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false, name = "error", defaultValue = "false") boolean error){
        Map<String, Object> model = new HashMap<>();
        if(error){
            model.put("error", true);
        }
        return new ModelAndView("login", model);
    }
}
