package com.osipov_evgeny.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Integer countGames;
    private Integer longestGame;

    public User() {
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

    public Integer getLongestGame() {
        return longestGame;
    }

    public void setLongestGame(Integer bestScore) {
        this.longestGame = bestScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(countGames, user.countGames) && Objects.equals(longestGame, user.longestGame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, countGames, longestGame);
    }
}
