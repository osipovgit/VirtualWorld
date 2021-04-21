package com.osipov_evgeny.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    // TODO: отображение страниц

    @GetMapping("/")
    public String homePage(Model model){
        return "home_page";
    }

    @GetMapping("/auth")
    public String authorization(Model model){
        return "authorization";
    }

    @GetMapping("/join")
    public String registration(Model model){
        return "registration.html";
    }

    @GetMapping("/simulation")
    public String simulationView(Model model){
        return "world_simulation";
    }

    @GetMapping("/result")
    public String resultView(Model model){
        return "simulation_result";
    }
}
