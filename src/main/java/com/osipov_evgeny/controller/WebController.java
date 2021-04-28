package com.osipov_evgeny.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage(Model model){
        return "home_page";
    }

    @GetMapping("/auth")
    public String authorization(Model model){
        return "signup_and_login";
    }

    @GetMapping("/join")
    public String registration(Model model){
        return "signup_and_login";
    }

    @GetMapping("/simulation")
    public String simulationView(Model model){
        return "world_simulation";
    }
}
