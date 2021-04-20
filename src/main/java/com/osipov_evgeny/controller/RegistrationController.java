package com.osipov_evgeny.controller;

import com.osipov_evgeny.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    //    TODO: регистрация
    //    TODO: авторизация
    //          cookie?

    @Autowired
    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

//    @RequestMapping("/auth")
//    public String authorization(Model model){
//        return "authorization";
//    }
//
//    @RequestMapping("/join")
//    public String registration(Model model){
//        return "registration";
//    }
}
