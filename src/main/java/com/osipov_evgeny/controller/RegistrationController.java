package com.osipov_evgeny.controller;

import com.osipov_evgeny.repository.UserRepo;
import org.springframework.stereotype.Controller;

@Controller
public class RegistrationController {
    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

//    TODO: регистрация
//    TODO: авторизация
}
