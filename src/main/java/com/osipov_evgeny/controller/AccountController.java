package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.User;
import com.osipov_evgeny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {
    //    TODO: exception messages

    @Autowired
    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/auth")
    public String authorization(User user, HttpServletRequest request, HttpServletResponse response, Model model){
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase == null) {
            return "redirect:/auth";
        }
        else if (userFromDatabase.getUsername().equals(user.getUsername())
                && userFromDatabase.getPassword().equals(user.getPassword())) {
            response.addCookie(new Cookie("user_id", userFromDatabase.getId().toString()));
            return "redirect:/";
        }
        return "redirect:/auth";
    }

    @PostMapping("/join")
    public String registration(User user, HttpServletResponse response, Model model) {
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        System.out.println(user);
        if (userFromDatabase == null) {
            userRepository.save(user);
            user = userRepository.findByUsername(user.getUsername());
            response.addCookie(new Cookie("user_id", user.getId().toString()));
            return "redirect:/";
        }
        return "redirect:/join";
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Model model){
        Cookie cookie = new Cookie("user_id", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
//        return "redirect:/auth";
    }
}
