package com.evgesha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Math.random;

public class Doctor extends People {
    private Integer talent;

    public Doctor() {
    }

    public Doctor(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
        this.setId(id);
        this.setProfession(profession);
        this.setAge(age);
        this.setDeadAge(deadAge);
        this.setSex(sex);
        this.setHealth(health);
        this.setIdMarriage(idMarriage);
        this.talent = 1 + (int) (random() * 10);
    }

    public Integer getTalent() {
        return talent;
    }

    public void setTalent(Integer talent) {
        this.talent = talent;
    }

    void toTableDoctor() throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO doctor (`id`, `profession`, `age`, `deadAge`, `sex`, `health`, `idMarriage`, `talent`)" + " VALUE (" + super.getId() + ", '" + super.getProfession() + "', " + super.getAge() + "," + super.getDeadAge() + ",'" + super.getSex() + "'," + super.getHealth() + "," + super.getIdMarriage() + "," + getTalent() + ");");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

}
