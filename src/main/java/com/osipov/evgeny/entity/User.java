package com.osipov.evgeny.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer countGames;
    private Integer longestGame;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    private SimulationSession simulationSession;

    public User() {
        this.countGames = 0;
        this.longestGame = 0;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.countGames = 0;
        this.longestGame = 0;
    }

    public void increaseCountGames() {
        this.countGames += 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCountGames() {
        return countGames;
    }

    public void setCountGames(Integer countGames) {
        this.countGames = countGames;
    }

    public Integer getLongestGame() {
        return longestGame;
    }

    public void setLongestGame(Integer bestScore) {
        this.longestGame = bestScore;
    }

    public SimulationSession getSimulationSession() {
        return simulationSession;
    }

    public void setSimulationSession(SimulationSession simulationSession) {
        this.simulationSession = simulationSession;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\"=\"" + id +
                "\", \"username\"=\"" + username +
                "\", \"countGames\"=" + countGames +
                ", \"longestGame\"=" + longestGame +
                '}';
    }

}
