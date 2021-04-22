package com.osipov_evgeny.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = "players_character")
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private InnateTalent profession;
    private Integer age;
    private Integer deadAge;
    private String sex;
    private Integer health;
    private Integer idMarriage;
    private Integer talent;

    public PlayerCharacter() {
    }

    public PlayerCharacter(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health) {
        this.userId = userId;
        this.profession = profession;
        this.age = 0;
        this.deadAge = deadAge;
        this.sex = sex;
        this.health = health;
        this.idMarriage = null;
        this.talent = 0;
    }

    public void becomeAYearOlder(Integer age) {
        this.age += 1;
    }

    public static PlayerCharacter generateRandomPlayerCharacter(Long userId) {
        String[] randomSex = {"man", "woman"};
        return new PlayerCharacter(userId, InnateTalent.getRandomRole(), new Random().nextInt(150),
                randomSex[new Random().nextInt(2)], new Random().nextInt(101) + 50);
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public InnateTalent getProfession() {
        return profession;
    }

    public void setProfession(InnateTalent profession) {
        this.profession = profession;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDeadAge() {
        return deadAge;
    }

    public void setDeadAge(Integer deadAge) {
        this.deadAge = deadAge;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getIdMarriage() {
        return idMarriage;
    }

    public void setIdMarriage(Integer idMarriage) {
        this.idMarriage = idMarriage;
    }

    public Integer getTalent() {
        return talent;
    }

    public void setTalent(Integer talent) {
        this.talent = talent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCharacter that = (PlayerCharacter) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && profession == that.profession && Objects.equals(age, that.age) && Objects.equals(deadAge, that.deadAge) && Objects.equals(sex, that.sex) && Objects.equals(health, that.health) && Objects.equals(idMarriage, that.idMarriage) && Objects.equals(talent, that.talent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, profession, age, deadAge, sex, health, idMarriage, talent);
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ", \"profession\":\"" + profession +
                "\", \"age\":" + age +
                ", \"sex\":\"" + sex + '\"' +
                ", \"health\":" + health +
                ", \"idMarriage\":\"" + idMarriage +
                "\", \"talent\":" + talent +
                '}';
    }
}
