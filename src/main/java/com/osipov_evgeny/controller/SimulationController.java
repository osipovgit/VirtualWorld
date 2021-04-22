package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.PlayerCharacter;
import com.osipov_evgeny.entity.User;
import com.osipov_evgeny.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    // TODO: get/post/put/delete методы? Разобрать тему!
    /* TODO: функционал игры:
             проход игрового цикла (год/квартал или месяц?)
             рождение нового жителя
             взаимодействие с едой (запасы на зимний период?)
             создание/развод пары (минимальный возраст?)
             смениа профессии по потребностям?
             приход новых жителей в поселение?
     */
    // TODO: улучшенный баланс
    // TODO: влияние пользователя на игру?

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PlayerCharacterRepository playerCharacterRepository;

    public SimulationController(UserRepository userRepository, PlayerCharacterRepository playerCharacterRepository) {
        this.userRepository = userRepository;
        this.playerCharacterRepository = playerCharacterRepository;
    }

    private User getUserFromCookie(HttpServletRequest request){
        Cookie cookie = null;
        for (Cookie cookies : request.getCookies()) {
            if (cookies.getName().equals("user_id")) {
                cookie = cookies;
                break;
            }
        }
        if (cookie == null) {
            return null;
        } else {
            return userRepository.findUserById(Long.parseLong(cookie.getValue()));
        }
    }

    @PostMapping("/display")
    public String initializeSimulationEntities(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        List<PlayerCharacter> playerCharacters = playerCharacterRepository.findAllByUserId(user.getId());
        if (playerCharacters.isEmpty()) {
            for (int i = 0; i < 5; ++i) {
                PlayerCharacter playerCharacter = PlayerCharacter.generateRandomPlayerCharacter(user.getId());
                playerCharacters.add(playerCharacter);
                playerCharacterRepository.save(playerCharacter);
            }
        }
        return playerCharacters.toString();
    }

//    @RequestMapping("/get_high_score_table")
//    public String getHighScoreTable(Model model){
//
//        return "?";
//    }

//    @RequestMapping("/simulation")
//    public String simulationView(Model model){
//        return "world_simulation";
//    }
//
//    @RequestMapping("/result")
//    public String resultView(Model model){
//        return "simulation_result";
//    }
}
