package com.osipov.evgeny.controller;

import com.osipov.evgeny.config.MappingConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage(Model model){
        return MappingConfig.homePage;
    }

    @GetMapping("/auth")
    public String authorization(Model model){
        return MappingConfig.signupAndLoginPage;
    }

    @GetMapping("/join")
    public String registration(Model model){
        return MappingConfig.signupAndLoginPage;
    }

    @GetMapping("/simulation")
    public String simulationView(Model model){
        return MappingConfig.simulationPage;
    }

}
