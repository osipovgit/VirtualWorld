package com.osipov_evgeny.entity;

import javax.persistence.*;
import java.util.Random;

@Entity
@Table(name = "players_character")
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long serialNumber;
    @ManyToOne
    @JoinColumn(name = "simulationSessionId")
    private SimulationSession simulationSessionId;
    @Enumerated(EnumType.STRING)
    private InnateTalent profession;
    private Integer age;
    private Integer deadAge;
    private Integer generation;
    private String sex;
    private Integer health;
    private Integer idMarriage;
    private Integer talent;

    public PlayerCharacter() {
    }

    public PlayerCharacter(SimulationSession simulationSessionId, Long serialNumber, InnateTalent profession, Integer generation) {
        String[] randomSex = {"man", "woman"};
        this.simulationSessionId = simulationSessionId;
        this.serialNumber = serialNumber;
        this.profession = profession;
        this.age = 0;
        this.deadAge = new Random().nextInt(101) + 50;
        this.generation = generation;
        this.sex = randomSex[new Random().nextInt(2)];
        this.health = new Random().nextInt(150);
        this.idMarriage = null;
        this.talent = 0;
    }

    public void becomeAYearOlder(Integer age) {
        this.age += 1;
    }

    public static PlayerCharacter generateRandomPlayerCharacter(SimulationSession simulationSessionId, Long serialNumber, Integer generation) {
        return new PlayerCharacter(simulationSessionId, serialNumber, InnateTalent.getRandomRole(), generation);
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public SimulationSession getSimulationSessionId() {
        return simulationSessionId;
    }

    public void setSimulationSessionId(SimulationSession simulationSessionId) {
        this.simulationSessionId = simulationSessionId;
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

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
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
    public String toString() {
        return "{\"id\":\"" + serialNumber +
                "\", \"profession\":\"" + profession +
                "\", \"age\":\"" + age +
                "\", \"generation\":\"" + generation +
                "\", \"sex\":\"" + sex +
                "\", \"health\":\"" + health +
                "\", \"idMarriage\":\"" + idMarriage +
                "\", \"talent\":\"" + talent +
                "\", \"pc_id\":\"" + id + "\"}";
    }
}
