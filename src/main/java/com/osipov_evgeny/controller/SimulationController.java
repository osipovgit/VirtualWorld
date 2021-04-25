package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.PlayerCharacter;
import com.osipov_evgeny.entity.SimulationSession;
import com.osipov_evgeny.entity.User;
import com.osipov_evgeny.repository.PlayerCharacterRepository;
import com.osipov_evgeny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private User getUserFromCookie(HttpServletRequest request) {
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

    private PlayerCharacter generateNewPlayerCharacter(User user, Integer generation) {
        PlayerCharacter newPlayer = PlayerCharacter.generateRandomPlayerCharacter(user.getSimulationSession(),
                user.getSimulationSession().getNextNumberOfPlayerCharacter(), generation);
        playerCharacterRepository.save(newPlayer);
        userRepository.save(user);
        return newPlayer;
    }

    private List<PlayerCharacter> initializePlayerCharacters(User user) {
        List<PlayerCharacter> players = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            PlayerCharacter playerCharacter = PlayerCharacter.generateRandomPlayerCharacter(user.getSimulationSession(),
                    user.getSimulationSession().getNextNumberOfPlayerCharacter(), 0);
            playerCharacter.setAge(new Random().nextInt(playerCharacter.getDeadAge() - 3));
            playerCharacter.setTalent(playerCharacter.getAge() / 5);
            players.add(playerCharacter);
            playerCharacterRepository.save(playerCharacter);
        }
        userRepository.save(user);
        return players;
    }

    private PlayerCharacter changeHealth(PlayerCharacter player) {

        return player;
    }

    @PostMapping("/display")
    public String displaySimulationEntities(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (user.getSimulationSession() == null) {
            user.setSimulationSession(new SimulationSession(user));
            userRepository.save(user);
            user = userRepository.findByUsername(user.getUsername());
        }

        List<PlayerCharacter> playerCharacters = playerCharacterRepository.findAllBySimulationSessionId(user.getSimulationSession());
        if (playerCharacters.isEmpty()) {
            playerCharacters = initializePlayerCharacters(user);
        }
        return playerCharacters.toString();
    }

    @PostMapping("/checking_all_actions")
    public String checkingAllActions(HttpServletRequest request, Model model) {
        // TODO: прописать условие (флаг/счетчики в SimulationSession?)
//        User user = getUserFromCookie(request);
//        if (user.getSimulationSession() == null) {
//            user.setSimulationSession(new SimulationSession(user));
//            userRepository.save(user);
//            user = userRepository.findByUsername(user.getUsername());
//        }
        return "false";
    }

    @PostMapping("/next_year")
    public String goToNextYear(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        generateNewPlayerCharacter(user, 0);
        return "Something is happening and what is not clear...";
    }
}
