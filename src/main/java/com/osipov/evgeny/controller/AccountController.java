package com.osipov.evgeny.controller;

import com.osipov.evgeny.aop.CookieCheck;
import com.osipov.evgeny.config.MappingConfig;
import com.osipov.evgeny.entity.User;
import com.osipov.evgeny.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AccountController {
    //    TODO: exception messages

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/auth")
    public String authorization(User user, HttpServletResponse response, Model model){
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase == null) {
            return MappingConfig.REDIRECT_AUTH;
        }
        else if (userFromDatabase.getUsername().equals(user.getUsername())
                && userFromDatabase.getPassword().equals(user.getPassword())) {
            Cookie cookie = new Cookie(CookieCheck.COOKIE_KEY, userFromDatabase.getId().toString());
            response.addCookie(cookie);
            return MappingConfig.REDIRECT_HOME;
        }
        return MappingConfig.REDIRECT_AUTH;
    }

    @PostMapping("/join")
    public String registration(User user, HttpServletResponse response, Model model) {
        User userFromDatabase = userRepository.findByUsername(user.getUsername());
        if (userFromDatabase == null) {
            userRepository.save(user);
            user = userRepository.findByUsername(user.getUsername());
            Cookie cookie = new Cookie(CookieCheck.COOKIE_KEY, user.getId().toString());
            response.addCookie(cookie);
            return MappingConfig.REDIRECT_HOME;
        }
        return MappingConfig.REDIRECT_JOIN;
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, Model model){
        Cookie cookie = new Cookie(CookieCheck.COOKIE_KEY, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return MappingConfig.REDIRECT_AUTH;
    }

}
