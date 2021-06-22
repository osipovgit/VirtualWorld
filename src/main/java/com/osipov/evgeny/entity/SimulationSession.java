package com.osipov.evgeny.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulation_sessions")
public class SimulationSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "owner", unique = true)
    private User owner;
    private Integer year;
    private Long numberOfNextPlayerCharacters;
    private Long foodSupplies;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "simulationSessionId", fetch = FetchType.EAGER)
    private List<PlayerCharacter> playerCharacter;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private List<SessionNotification> notifications;

    public SimulationSession() {
        this.year = 0;
        this.numberOfNextPlayerCharacters = 0L;
        this.foodSupplies = 45L;
    }

    public SimulationSession(User owner) {
        this.owner = owner;
        this.year = 0;
        this.numberOfNextPlayerCharacters = 0L;
        this.foodSupplies = 45L;
    }

    public Long getNextNumberOfPlayerCharacter() {
        this.numberOfNextPlayerCharacters += 1;
        return numberOfNextPlayerCharacters;
    }

    public void nextYear() {
        this.year += 1;
        for (PlayerCharacter player : playerCharacter) {
            player.setAge(player.getAge() + 1);
        }
    }

    public List<PlayerCharacter> getAllDead() {
        List<PlayerCharacter> players = new ArrayList<>();
        for (PlayerCharacter player : playerCharacter) {
            if (player.getModelState() == ModelState.DEAD) {
                players.add(player);
            }
        }
        return players;
    }

    public Integer countOfDoctorsActions() {
        Integer sumAllSpecialAction = 0;
        for (PlayerCharacter player : playerCharacter) {
            if (player.getModelState() == ModelState.DOCTOR) {
                sumAllSpecialAction += player.getSpecialAction();
            }
        }
        return sumAllSpecialAction;
    }

    public Integer countOfUnfinishedTasks() {
        Integer sumAllSpecialAction = 0;
        for (PlayerCharacter player : playerCharacter) {
            sumAllSpecialAction += player.getSpecialAction();
        }
        return sumAllSpecialAction;
    }

    public Boolean checkIfAllCasesHaveBeenCompleted() {
        return (countOfUnfinishedTasks() == 0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getNumberOfNextPlayerCharacters() {
        return numberOfNextPlayerCharacters;
    }

    public void setNumberOfNextPlayerCharacters(Long numberOfPlayerCharacters) {
        this.numberOfNextPlayerCharacters = numberOfPlayerCharacters;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getFoodSupplies() {
        return foodSupplies;
    }

    public void setFoodSupplies(Long foodSupplies) {
        this.foodSupplies = foodSupplies;
    }

    public List<PlayerCharacter> getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(List<PlayerCharacter> playerCharacter) {
        this.playerCharacter = playerCharacter;
    }

    public List<SessionNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<SessionNotification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "SimulationSession{" +
                "owner=" + owner +
                ", year=" + year +
                ", numberOfPlayerCharacters=" + numberOfNextPlayerCharacters +
                ", foodSupplies=" + foodSupplies +
                ", playerCharacter=" + playerCharacter +
                ", notifications=" + notifications +
                '}';
    }
}
