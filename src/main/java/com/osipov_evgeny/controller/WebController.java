package com.osipov_evgeny.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    // TODO: отображение страниц

    @RequestMapping("/")
    public String homePage(Model model){
        return "home_page";
    }

    @RequestMapping("/auth")
    public String authorization(Model model){
        return "authorization";
    }

    @RequestMapping("/join")
    public String registration(Model model){
        return "registration";
    }

    @RequestMapping("/simulation")
    public String simulationView(Model model){
        return "world_simulation";
    }

    @RequestMapping("/result")
    public String resultView(Model model){
        return "simulation_result";
    }
}
