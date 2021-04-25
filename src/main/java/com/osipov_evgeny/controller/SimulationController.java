package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.*;
import com.osipov_evgeny.repository.PlayerCharacterRepository;
import com.osipov_evgeny.repository.SessionNotificationRepository;
import com.osipov_evgeny.repository.SimulationSessionRepository;
import com.osipov_evgeny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    /* TODO: функционал игры:
             проход игрового цикла (год/квартал или месяц?)
             взаимодействие с едой (запасы на зимний период?)
             создание/развод пары (минимальный возраст?)
             смениа профессии по потребностям?
             приход новых жителей в поселение?
     */
    // TODO: улучшенный баланс
    // TODO: влияние пользователя на игру?

    private static final Integer heal = 10;
    private static final Integer food = 10;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SimulationSessionRepository sessionRepository;

    @Autowired
    private final PlayerCharacterRepository playerCharacterRepository;

    @Autowired
    private final SessionNotificationRepository notificationRepository;

    public SimulationController(UserRepository userRepository, SimulationSessionRepository sessionRepository, PlayerCharacterRepository playerCharacterRepository, SessionNotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.playerCharacterRepository = playerCharacterRepository;
        this.notificationRepository = notificationRepository;
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
        PlayerCharacter newPlayer = new PlayerCharacter(user.getSimulationSession(),
                user.getSimulationSession().getNextNumberOfPlayerCharacter(), generation);
        user.getSimulationSession().getPlayerCharacter().add(newPlayer);
        userRepository.save(user);
        return newPlayer;
    }

    private List<PlayerCharacter> initializePlayerCharacters(User user) {
        List<PlayerCharacter> players = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            PlayerCharacter playerCharacter = new PlayerCharacter(user.getSimulationSession(),
                    user.getSimulationSession().getNextNumberOfPlayerCharacter(), 0);
            playerCharacter.setProfession(InnateTalent.getRandomRole());
            playerCharacter.setAge(new Random().nextInt(playerCharacter.getDeadAge() - 3));
            playerCharacter.setTalent((playerCharacter.getAge() > 14 ? (playerCharacter.getAge() - 14) / 5 : 0));
            players.add(playerCharacter);
        }
        user.getSimulationSession().setPlayerCharacter(players);
        userRepository.save(user);
        return players;
    }

    @PostMapping("/display")
    public String displaySimulationEntities(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (user.getSimulationSession() == null) {
            user.setSimulationSession(new SimulationSession(user));
            userRepository.save(user);
            user = userRepository.findByUsername(user.getUsername());
        }

        List<PlayerCharacter> playerCharacters = user.getSimulationSession().getPlayerCharacter();
        if (playerCharacters == null || playerCharacters.isEmpty()) {
            playerCharacters = initializePlayerCharacters(user);
        }
        return playerCharacters.toString();
    }

    @PostMapping("/get_current_year")
    public String findOutTheCurrentYear(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().getYear().toString();
    }

    @PostMapping("/checking_all_actions")
    public String checkingAllActions(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().checkIfAllCasesHaveBeenCompleted().toString();
//        return "false";
    }

    @PostMapping("/raise_health")
    public String raiseHealth(@RequestBody String idFromRequestBody, HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (!user.getSimulationSession().checkIfAllCasesHaveBeenCompleted()) {
            Long playerCharacterId = Long.parseLong(new StringBuilder(idFromRequestBody).substring(3));
            for (PlayerCharacter doctorPC : user.getSimulationSession().getPlayerCharacter()) {
                if (doctorPC.getProfession() == InnateTalent.DOCTOR && doctorPC.getSpecialAction() != 0) {
                    PlayerCharacter player = playerCharacterRepository.getOne(playerCharacterId);
                    player.setHealth(player.getHealth() + heal * doctorPC.getTalent());
                    doctorPC.setSpecialAction(0);
                    user.getSimulationSession().getNotifications().add(
                            new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | Doctor raised " + playerCharacterId
                                    + "'s health level by " + heal * doctorPC.getTalent()));
                    playerCharacterRepository.save(player);
                    userRepository.save(user);
                    break;
                }
            }
            return "";
        } else {
            return "All actions have already been completed. Go to next year!";
        }
    }

    public void sheriffIsLookingForACriminal(List<PlayerCharacter> playerCharacters, PlayerCharacter player, User user) {
        playerCharacters.remove(player);
        PlayerCharacter randomPlayer = playerCharacters.get(new Random().nextInt(playerCharacters.size()));
        if (randomPlayer.getProfession() == InnateTalent.CRIMINAL) {
            randomPlayer.setProfession(InnateTalent.CAUGHT);
            notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                    user.getSimulationSession().getYear() + "y.  | Sheriff caught criminal with ID: "
                            + randomPlayer.getId()));
            System.out.println("find criminal with id = " + randomPlayer.getId());
        }
        playerCharacters.add(player);
    }
    @PostMapping("/get_messages")
    public String getMessages(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().getNotifications().toString();
    }

    @PostMapping("/next_year")
    public String goToNextYear(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (user.getSimulationSession().checkIfAllCasesHaveBeenCompleted()) {
            user.getSimulationSession().getNextYear();
            playerCharacterRepository.becomeAYearOlder(user.getSimulationSession());
            for(PlayerCharacter player : user.getSimulationSession().getPlayerCharacter()) {
                if (player.getAge().equals(player.getDeadAge())) {
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " character died. He was " + player.getAge() + " years old."));
                    player.setProfession(InnateTalent.DEAD);
                }
//            farmer
//            criminal
//                SHERIFF
//                if (player.getProfession() == InnateTalent.SHERIFF) {
//                    sheriffIsLookingForACriminal(user.getSimulationSession().getPlayerCharacter(), player, user);
//                } TODO error (loop?)
//                DOCTOR
                if (player.getProfession() == InnateTalent.DOCTOR) {
                    player.setSpecialAction(1);
                }
//            prisoner

            }
//            marriage
//            eat
            sessionRepository.save(user.getSimulationSession());
            playerCharacterRepository.deleteAllByProfessionAndSessionId(InnateTalent.DEAD.toString(),
                    user.getSimulationSession().getId());
            //TODO закончить игру, если PC нет, обновить результаты, удалить сессию
            return "";
        } else {
            return "Wait until you've, finished";
        }
    }
}
