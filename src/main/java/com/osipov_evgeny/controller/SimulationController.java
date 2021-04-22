package com.osipov_evgeny.controller;

import com.osipov_evgeny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public SimulationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/get_high_score_table")
    public String getHighScoreTable(Model model){

        return "?";
    }

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
