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

    private static final Integer heal = 10;     // heal + doctor.getTalent()
    private static final Integer food = 10;     // food + farmer.getTalent()
    private static final Integer damage = 10;     // damage + criminal.getTalent()
    private static final Integer lowerToKill = 1;   // (randomValue < lowerToKill)   -> kill
    private static final Integer upperToDamage = 4; // (randomValue > upperToDamage) -> damage []
    private static final Integer upperBoundOfRandomValue = 10; // [0, upperBound) of randomValue

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

    public void criminalIsDoingSomethingBad (List<PlayerCharacter> playerCharacters, PlayerCharacter player, User user) {
        playerCharacters.remove(player);    // удаляем самого преступника из списка
        int randomIndex = new Random().nextInt(playerCharacters.size()); // получаем случайную жертву
        if (playerCharacters.get(randomIndex).getProfession() != InnateTalent.CAUGHT
                && playerCharacters.get(randomIndex).getProfession() != InnateTalent.PRISONER
                && playerCharacters.get(randomIndex).getProfession() != InnateTalent.DEAD) {
            PlayerCharacter randomPlayer = playerCharacters.get(randomIndex);
            int randomValue = new Random().nextInt(upperBoundOfRandomValue);
            if (randomValue < lowerToKill) {
                notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                        user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                + " killed " + randomPlayer.getAge() + " year old " + randomPlayer.getProfession()));
                randomPlayer.setProfession(InnateTalent.DEAD);
                playerCharacterRepository.save(randomPlayer);
            } else if (randomValue > upperToDamage) {
                randomPlayer.setHealth(randomPlayer.getHealth() - (damage + randomPlayer.getTalent()));
                notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                        user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                + " deals " + (damage + randomPlayer.getTalent()) + " damage to "
                                + randomPlayer.getAge() + " year old " + randomPlayer.getProfession()));
            }   // иначе преступник ничего не делает
        }
    }

    public void sheriffIsLookingForACriminal (List<PlayerCharacter> playerCharacters, PlayerCharacter player, User user) {
        playerCharacters.remove(player); // удаляем самого шерифа из списка
        int randomIndex = new Random().nextInt(playerCharacters.size()); // получаем случайного персонажа
        if (playerCharacters.get(randomIndex).getProfession() == InnateTalent.CRIMINAL) { // проверяем его роль
            playerCharacters.get(randomIndex).setProfession(InnateTalent.CAUGHT); // роль "пойман" (далее решает юзер)
            notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                    user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                            + " caught criminal with ID: " + playerCharacters.get(randomIndex).getSerialNumber()));
            playerCharacterRepository.save(playerCharacters.get(randomIndex)); // сохраняем персонажа с новой ролью
        }
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

    @PostMapping("/get_current_state")
    public String findOutTheCurrentYear(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        return "{\"Year\": " + user.getSimulationSession().getYear().toString()
                + ", \"Doctor's visits\": " + user.getSimulationSession().countOfUnfinishedTasks()
                + ", \"Food supplies\": " + user.getSimulationSession().getFoodSupplies() + '}';
    }

    @PostMapping("/checking_all_actions")
    public String checkingAllActions(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().checkIfAllCasesHaveBeenCompleted().toString();
    }

    @PostMapping("/get_actions")
    public String getActions(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        String actionsJson = "{";
        for (PlayerCharacter player : user.getSimulationSession().getPlayerCharacter()) {
            if (player.getProfession() == InnateTalent.CAUGHT) {
                actionsJson += "\"caught\":\"ID: " + player.getSerialNumber() + " is caught\", ";
            } else if (player.getProfession() == InnateTalent.VILLAGER) { // TODO random value
                actionsJson += "\"profession\":\"ID: " + player.getSerialNumber() + " prepares to work:\", ";
            }
        }
        return actionsJson + "\"end\":\"end\"}";
    }

    @PostMapping("/get_messages")
    public String getMessages(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().getNotifications().toString();
    }

    @PostMapping("/raise_health")
    public String raiseHealth(@RequestBody String idFromRequestBody, HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (!user.getSimulationSession().checkIfAllCasesHaveBeenCompleted()) {
            Long playerCharacterId = Long.parseLong(new StringBuilder(idFromRequestBody).substring(3));
            for (PlayerCharacter doctorPC : user.getSimulationSession().getPlayerCharacter()) {
                if (doctorPC.getProfession() == InnateTalent.DOCTOR && doctorPC.getSpecialAction() != 0) {
                    PlayerCharacter player = playerCharacterRepository.getOne(playerCharacterId);
                    player.setHealth(player.getHealth() + heal + doctorPC.getTalent());
                    doctorPC.setSpecialAction(0);
                    user.getSimulationSession().getNotifications().add(
                            new SessionNotification(user.getSimulationSession(),
                                    user.getSimulationSession().getYear() + "y.  | Doctor raised " + player.getSerialNumber()
                                            + "'s health level by " + (heal + doctorPC.getTalent())));
                    playerCharacterRepository.save(player);
                    userRepository.save(user);
                    break;
                }
            }
            return "";
        } else {
            return "All actions have already been completed.";
        }
    }

    @PostMapping("/next_year")
    public String goToNextYear(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (user.getSimulationSession().checkIfAllCasesHaveBeenCompleted()) {
            user.getSimulationSession().nextYear();
            for (PlayerCharacter player : user.getSimulationSession().getPlayerCharacter()) {
                if (player.getAge().equals(player.getDeadAge())) {
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " character died. He was " + player.getAge() + " years old."));
                    player.setProfession(InnateTalent.DEAD);
                }
//            FARMER
                if (player.getProfession() == InnateTalent.FARMER) {
                    user.getSimulationSession().setFoodSupplies(user.getSimulationSession().getFoodSupplies()
                            + (food + player.getTalent()));
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " farmer " + player.getSerialNumber() + "'s has harvested: "
                                    + (food + player.getTalent()) + " point of food."));
                }
//                CRIMINAL
                if (player.getProfession() == InnateTalent.CRIMINAL
                        && user.getSimulationSession().getPlayerCharacter().size() != 1) {
                    criminalIsDoingSomethingBad(new ArrayList<>(user.getSimulationSession().getPlayerCharacter()),
                            player, user);
                }
//                SHERIFF
                if (player.getProfession() == InnateTalent.SHERIFF
                        && user.getSimulationSession().getPlayerCharacter().size() != 1) {
                    sheriffIsLookingForACriminal(new ArrayList<>(user.getSimulationSession().getPlayerCharacter()),
                            player, user);
                }
//                DOCTOR
                if (player.getProfession() == InnateTalent.DOCTOR) {
                    player.setSpecialAction(1);
                }
//                prisoner
                if (player.getProfession() == InnateTalent.PRISONER) {

                }
            }
//            marriage
//            eat
//            talent

            sessionRepository.save(user.getSimulationSession()); // сохраняем изменения после цикла изменений
            playerCharacterRepository.deleteInBatch(user.getSimulationSession().getAllDEAD()); // удаляем мертвых (DEAD)

            user = userRepository.findUserById(user.getId()); // обновление данных пользователя, сессии и персонажей
            SimulationSession session = sessionRepository.findSimulationSessionByOwner(user);
            session.setPlayerCharacter(playerCharacterRepository.findAllBySimulationSessionId(session));

            // проверка оставшихся персонажей (empty == game over)
            if (session.getPlayerCharacter().isEmpty() || session.getPlayerCharacter() == null) {
                String results = "Simulation lasted " + session.getYear() + " years. Population: "
                        + session.getNumberOfNextPlayerCharacters();
                user.setCountGames(user.getCountGames() + 1);
                user.setLongestGame(Math.max(user.getSimulationSession().getYear(), user.getLongestGame()));
                user.setSimulationSession(null);

                userRepository.save(user);
                sessionRepository.deleteSimulationSessionById(session.getId());
                return results;
            }
            return "";
        } else {
            return "Wait until you've, finished";
        }
    }
}
