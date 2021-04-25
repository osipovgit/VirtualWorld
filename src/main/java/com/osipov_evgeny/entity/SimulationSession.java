package com.osipov_evgeny.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "simulation_sessions")
public class SimulationSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "owner")
    private User owner;
    private Long numberOfPlayerCharacters;
    private Long foodSupplies;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "simulationSessionId")
    private List<PlayerCharacter> playerCharacter;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionId")
    private List<SessionNotification> notifications;

    public SimulationSession() {
        this.numberOfPlayerCharacters = 0L;
        this.foodSupplies = 45L;
    }

    public SimulationSession(User owner) {
        this.owner = owner;
        this.numberOfPlayerCharacters = 0L;
        this.foodSupplies = 45L;
    }

    public Long getNextNumberOfPlayerCharacter() {
        this.numberOfPlayerCharacters += 1;
        return numberOfPlayerCharacters;
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

    public Long getNumberOfPlayerCharacters() {
        return numberOfPlayerCharacters;
    }

    public void setNumberOfPlayerCharacters(Long numberOfPlayerCharacters) {
        this.numberOfPlayerCharacters = numberOfPlayerCharacters;
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
                ", numberOfPlayerCharacters=" + numberOfPlayerCharacters +
                ", foodSupplies=" + foodSupplies +
                ", playerCharacter=" + playerCharacter +
                ", notifications=" + notifications +
                '}';
    }
}
