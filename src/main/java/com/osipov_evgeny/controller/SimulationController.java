package com.osipov_evgeny.controller;

import com.osipov_evgeny.entity.*;
import com.osipov_evgeny.repository.PlayerCharacterRepository;
import com.osipov_evgeny.repository.SessionNotificationRepository;
import com.osipov_evgeny.repository.SimulationSessionRepository;
import com.osipov_evgeny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    /* TODO: функционал игры:
             создание/развод пары (минимальный возраст?)
             приход новых жителей в поселение?
     */
    // TODO: улучшенный баланс

    private static final Integer HEAL = 10;     // HEAL + doctor.getTalent()
    private static final Integer FOOD = 10;     // FOOD + farmer.getTalent()
    private static final Integer DAMAGE = 10;     // DAMAGE + criminal.getTalent()
    private static final Integer FOOD_PER_PLAYER_CHARACTER = 5;     //
    private static final Integer HEALTH_PER_UNIT_OF_FOOD = 5;     //
    private static final Integer LOWER_TO_SET_PROFESSION = 1; // (randomValue < LOWER_TO_SET_PROFESSION) -> VILLAGER change profession
    private static final Integer LOWER_TO_KILL = 1;   // (randomValue < lowerToKill)   -> kill
    private static final Integer UPPER_TO_DAMAGE = 4; // (randomValue > upperToDamage) -> damage []
    private static final Integer UPPER_BOUND_OF_RANDOM_VALUE = 10; // [0, upperBound) of randomValue

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SimulationSessionRepository sessionRepository;

    @Autowired
    private final PlayerCharacterRepository playerCharacterRepository;

    @Autowired
    private final SessionNotificationRepository notificationRepository;

    public SimulationController(UserRepository userRepository, SimulationSessionRepository sessionRepository,
                                PlayerCharacterRepository playerCharacterRepository,
                                SessionNotificationRepository notificationRepository) {
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

    private void criminalIsDoingSomethingBad(List<PlayerCharacter> playerCharacters,
                                             PlayerCharacter player, User user) {
        playerCharacters.remove(player);    // удаляем самого преступника из списка
        int randomIndex = new Random().nextInt(playerCharacters.size()); // получаем случайную жертву
        if (playerCharacters.get(randomIndex).getProfession() != InnateTalent.CAUGHT
                && playerCharacters.get(randomIndex).getProfession() != InnateTalent.PRISONER
                && playerCharacters.get(randomIndex).getProfession() != InnateTalent.DEAD
                && playerCharacters.get(randomIndex).getProfession() != null) {
            PlayerCharacter randomPlayer = playerCharacters.get(randomIndex);
            int randomValue = new Random().nextInt(UPPER_BOUND_OF_RANDOM_VALUE);
            if (randomValue < LOWER_TO_KILL) {
                notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                        user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                + " killed " + randomPlayer.getAge() + " year old " + randomPlayer.getProfession()));
                randomPlayer.setProfession(InnateTalent.DEAD);
                playerCharacterRepository.save(randomPlayer);
            } else if (randomValue > UPPER_TO_DAMAGE) {
                randomPlayer.setHealth(randomPlayer.getHealth() - (DAMAGE + randomPlayer.getTalent()));
                notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                        user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                + " deals " + (DAMAGE + randomPlayer.getTalent()) + " damage to "
                                + randomPlayer.getAge() + " year old " + randomPlayer.getProfession()));
            }   // иначе преступник ничего не делает
        }
    }

    private void sheriffIsLookingForACriminal(List<PlayerCharacter> playerCharacters,
                                              PlayerCharacter player, User user) {
        playerCharacters.remove(player); // удаляем самого шерифа из списка
        int randomIndex = new Random().nextInt(playerCharacters.size()); // получаем случайного персонажа
        if (playerCharacters.get(randomIndex).getProfession() == InnateTalent.CRIMINAL) { // проверяем его роль
            playerCharacters.get(randomIndex).setProfession(InnateTalent.CAUGHT); // роль "пойман" (далее решает юзер)
            playerCharacters.get(randomIndex).setSpecialAction(1);
            notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                    user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                            + " caught criminal with ID: " + playerCharacters.get(randomIndex).getSerialNumber()));
            playerCharacterRepository.save(playerCharacters.get(randomIndex)); // сохраняем персонажа с новой ролью
        }
    }

    private void foodDistribution(SimulationSession session) {
        Collections.shuffle(session.getPlayerCharacter());
        for (PlayerCharacter player : session.getPlayerCharacter()) {
            int shareOfTotalFoodSupply = FOOD_PER_PLAYER_CHARACTER;
            switch (player.getProfession()) {
                case VILLAGER:
                    shareOfTotalFoodSupply /= 2;
                    break;
                case PRISONER:
                    shareOfTotalFoodSupply *= 2;
                    break;
                case DEAD:
                    continue;
            }

            session.setFoodSupplies(session.getFoodSupplies() - shareOfTotalFoodSupply);
            player.setHealth((int) (player.getHealth() + (session.getFoodSupplies() >= 0 ? HEALTH_PER_UNIT_OF_FOOD: session.getFoodSupplies() * HEALTH_PER_UNIT_OF_FOOD)));
            if (session.getFoodSupplies() < 0) {
                session.setFoodSupplies(0L);
            }
        }
        sessionRepository.save(session);
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
                + ", \"Doctor's visits\": " + user.getSimulationSession().countOfDoctorsActions()
                + ", \"Food supplies\": " + user.getSimulationSession().getFoodSupplies() + '}';
    }

    @PostMapping("/checking_all_actions")
    public String checkingAllActions(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().checkIfAllCasesHaveBeenCompleted().toString();
    }

    @PostMapping("/checking_doctor_actions")
    public String checkingDoctorActions(HttpServletRequest request, Model model) {
        return "" + (getUserFromCookie(request).getSimulationSession().countOfDoctorsActions() > 0);
    }

    @PostMapping("/get_actions")
    public String getActions(HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        String actionsJson = "{";
        for (PlayerCharacter player : user.getSimulationSession().getPlayerCharacter()) {
            if (player.getProfession() == InnateTalent.CAUGHT) {
                actionsJson += "\"caught\":\"" + player.getSerialNumber() + "\", ";
            } else if (player.getProfession() == null) {
                actionsJson += "\"profession\":\"" + player.getSerialNumber() + "\", ";
            }
        }
        return actionsJson + "\"end\":\"end\"}";
    }

    @PostMapping("/decide_fate")
    public String userChangeProfessionFromVillagerOrCaught(@RequestParam("id") Long id,
                                                           @RequestParam("destiny") InnateTalent profession,
                                                           HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        PlayerCharacter playerCharacter = playerCharacterRepository.findBySimulationSessionIdAndSerialNumber(
                getUserFromCookie(request).getSimulationSession(), id);
        if (profession == InnateTalent.DEAD) {
            notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                    user.getSimulationSession().getYear() + "y.  | You chose to kill the CRIMINAL with ID "
                            + id + "."));
            playerCharacterRepository.deleteBySimulationSessionIdAndId(user.getSimulationSession(), playerCharacter.getId());
        } else {
            System.out.println(playerCharacter);
            playerCharacter.setProfession(profession);
            playerCharacter.setSpecialAction(0);
            playerCharacter.setTalent(0);
            playerCharacterRepository.save(playerCharacter);
            notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                    user.getSimulationSession().getYear() + "y.  | Now " + id + "'s is a " + profession + "."));
        }
        return "";
    }

    @PostMapping("/get_messages")
    public String getMessages(HttpServletRequest request, Model model) {
        return getUserFromCookie(request).getSimulationSession().getNotifications().toString();
    }

    @PostMapping("/raise_health")
    public String raiseHealth(@RequestParam("id") Long playerCharacterId, HttpServletRequest request, Model model) {
        User user = getUserFromCookie(request);
        if (user.getSimulationSession().countOfDoctorsActions() > 0) {
            for (PlayerCharacter doctorPC : user.getSimulationSession().getPlayerCharacter()) {
                if (doctorPC.getProfession() == InnateTalent.DOCTOR && doctorPC.getSpecialAction() != 0) {
                    PlayerCharacter player = playerCharacterRepository.getOne(playerCharacterId);
                    player.setHealth(player.getHealth() + HEAL + doctorPC.getTalent());
                    doctorPC.setSpecialAction(0);
                    user.getSimulationSession().getNotifications().add(
                            new SessionNotification(user.getSimulationSession(),
                                    user.getSimulationSession().getYear() + "y.  | Doctor raised " + player.getSerialNumber()
                                            + "'s health level by " + (HEAL + doctorPC.getTalent())));
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
            foodDistribution(user.getSimulationSession());
            user = userRepository.getOne(user.getId());
            user.getSimulationSession().nextYear();
            for (PlayerCharacter player : user.getSimulationSession().getPlayerCharacter()) {
                // Death condition
                if (player.getAge().equals(player.getDeadAge()) || player.getHealth() <= 0) {
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " character died. He was " + player.getAge() + " years old."));
                    player.setProfession(InnateTalent.DEAD);
                }
                // FARMER
                if (player.getProfession() == InnateTalent.FARMER) {
                    user.getSimulationSession().setFoodSupplies(user.getSimulationSession().getFoodSupplies()
                            + (FOOD + player.getTalent()));
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " with ID " + player.getSerialNumber() + " has harvested: "
                                    + (FOOD + player.getTalent()) + " point of food."));
                }
                // CRIMINAL
                if (player.getProfession() == InnateTalent.CRIMINAL
                        && user.getSimulationSession().getPlayerCharacter().size() != 1) {
                    criminalIsDoingSomethingBad(new ArrayList<>(user.getSimulationSession().getPlayerCharacter()),
                            player, user);
                }
                // SHERIFF
                if (player.getProfession() == InnateTalent.SHERIFF
                        && user.getSimulationSession().getPlayerCharacter().size() != 1) {
                    sheriffIsLookingForACriminal(new ArrayList<>(user.getSimulationSession().getPlayerCharacter()),
                            player, user);
                }
                // DOCTOR
                if (player.getProfession() == InnateTalent.DOCTOR) {
                    player.setSpecialAction(1);
                }
                // VILLAGER
                if (player.getProfession() == InnateTalent.VILLAGER
                        && new Random().nextInt(UPPER_BOUND_OF_RANDOM_VALUE) < LOWER_TO_SET_PROFESSION) {
                    notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                            user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                    + " with ID " + player.getSerialNumber() + " is open to work! Help him with choice!"));
                    player.setProfession(null);
                    player.setSpecialAction(1);
                }
                // PRISONER
                if (player.getProfession() == InnateTalent.PRISONER) {
                    if (player.getTalent() == 15) {
                        player.setTalent(0);
                        notificationRepository.save(new SessionNotification(user.getSimulationSession(),
                                user.getSimulationSession().getYear() + "y.  | " + player.getProfession()
                                        + " with ID " + player.getSerialNumber() + " reformed in prison and "
                                        + "starts a different life. Now: VILLAGER"));
                        player.setProfession(InnateTalent.VILLAGER);
                    } else {
                        player.setTalent(player.getTalent() + 1);
                    }
                }
            }
//            marriage
//            eat
//            talent
            // изменение профессии на criminal

            sessionRepository.save(user.getSimulationSession()); // сохраняем изменения после цикла изменений
            playerCharacterRepository.deleteInBatch(user.getSimulationSession().getAllDead()); // удаляем мертвых (DEAD)

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
