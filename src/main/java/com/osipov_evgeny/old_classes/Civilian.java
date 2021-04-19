package com.osipov_evgeny.old_classes;

import java.sql.*;

public class Civilian extends People {
    public Civilian() {

    }

    public Civilian(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
        this.setId(id);
        this.setProfession(profession);
        this.setAge(age);
        this.setDeadAge(deadAge);
        this.setSex(sex);
        this.setHealth(health);
        this.setIdMarriage(idMarriage);
    }

    void toTableCivilian() throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO civilian (`id`, `profession`, `age`, `deadAge`, `sex`, `health`, `idMarriage`)" + " VALUE (" + super.getId() + ",'" + super.getProfession() + "'," + super.getAge() + "," + super.getDeadAge() + ",'" + super.getSex() + "'," + super.getHealth() + "," + super.getIdMarriage() + ");");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }
}
