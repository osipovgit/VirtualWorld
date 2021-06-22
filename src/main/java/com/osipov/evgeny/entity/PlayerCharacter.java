package com.osipov.evgeny.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = "player_characters")
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long serialNumber;
    @ManyToOne
    @JoinColumn(name = "simulationSessionId")
    private SimulationSession simulationSessionId;
    @Enumerated(EnumType.STRING)
    private ModelState modelState;
    private Integer age;
    private Integer deadAge;
    private Integer generation;
    private String sex;
    private Integer health;
    private Long idMarriage;
    private Integer talent;
    private Integer specialAction;

    public PlayerCharacter() {
        String[] randomSex = {"man", "woman"};
        this.modelState = ModelState.VILLAGER;
        this.age = 0;
        this.deadAge = new Random().nextInt(101) + 50;
        this.sex = randomSex[new Random().nextInt(2)];
        this.health = new Random().nextInt(150);
        this.idMarriage = null;
        this.talent = 0;
        this.specialAction = 0;
    }

    public PlayerCharacter(SimulationSession simulationSessionId, Long serialNumber, Integer generation) {
        String[] randomSex = {"man", "woman"};
        this.simulationSessionId = simulationSessionId;
        this.serialNumber = serialNumber;
        this.modelState = ModelState.VILLAGER;
        this.age = 0;
        this.deadAge = new Random().nextInt(101) + 50;
        this.generation = generation;
        this.sex = randomSex[new Random().nextInt(2)];
        this.health = new Random().nextInt(150);
        this.idMarriage = null;
        this.talent = 0;
        this.specialAction = 0;
    }

    public void becomeAYearOlder() {
        this.age += 1;
    }

    public Long getId() {
        return id;
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

    public ModelState getModelState() {
        return modelState;
    }

    public void setModelState(ModelState modelState) {
        this.modelState = modelState;
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

    public Long getIdMarriage() {
        return idMarriage;
    }

    public void setIdMarriage(Long idMarriage) {
        this.idMarriage = idMarriage;
    }

    public Integer getTalent() {
        return talent;
    }

    public void setTalent(Integer talent) {
        this.talent = talent;
    }

    public Integer getSpecialAction() {
        return specialAction;
    }

    public void setSpecialAction(Integer specialAction) {
        this.specialAction = specialAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCharacter that = (PlayerCharacter) o;
        return Objects.equals(id, that.id) && Objects.equals(serialNumber, that.serialNumber) && Objects.equals(simulationSessionId, that.simulationSessionId) && modelState == that.modelState && Objects.equals(age, that.age) && Objects.equals(deadAge, that.deadAge) && Objects.equals(generation, that.generation) && Objects.equals(sex, that.sex) && Objects.equals(health, that.health) && Objects.equals(idMarriage, that.idMarriage) && Objects.equals(talent, that.talent) && Objects.equals(specialAction, that.specialAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, simulationSessionId, modelState, age, deadAge, generation, sex, health, idMarriage, talent, specialAction);
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + serialNumber +
                "\", \"model state\":\"" + modelState +
                "\", \"age\":\"" + age +
                "\", \"generation\":\"" + generation +
                "\", \"sex\":\"" + sex +
                "\", \"health\":\"" + health +
                "\", \"idMarriage\":\"" + (idMarriage == null ? "-" : idMarriage) +
                "\", \"talent\":\"" + talent +
                "\", \"pc_id\":\"" + id + "\"}";
    }
}
