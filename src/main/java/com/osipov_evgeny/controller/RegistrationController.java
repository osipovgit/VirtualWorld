package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.User;
import com.osipov_evgeny.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RegistrationController {
    //    TODO: exception messages
    //          cookie?

    @Autowired
    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/auth")
    public String authorization(User user, HttpServletResponse response, Model model){
        User userFromDatabase = userRepo.findByUsername(user.getUsername());
        if (userFromDatabase == null) {
            return "redirect:/auth";
        }
        else if (userFromDatabase.getUsername().equals(user.getUsername())
                && userFromDatabase.getPassword().equals(user.getPassword())) {
            return "redirect:/";
        }
        return "redirect:/auth";
    }

    @PostMapping("/join")
    public String registration(User user, HttpServletResponse response, Model model) {
        User userFromDatabase = userRepo.findByUsername(user.getUsername());
        System.out.println(user);
        if (userFromDatabase == null) {
            userRepo.save(user);
            return "redirect:/";
        }
        return "redirect:/join";
    }
}
