package com.evgesha;

import java.sql.*;

import static java.lang.Math.random;

public class Criminal extends People {
    private Integer danger;

    public Criminal() {

    }

    public Criminal(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
        this.setId(id);
        this.setProfession(profession);
        this.setAge(age);
        this.setDeadAge(deadAge);
        this.setSex(sex);
        this.setHealth(health);
        this.setIdMarriage(idMarriage);
        if ((int) (random() * 9) < 8) {
            this.danger = 0;
        } else {
            this.danger = 1;
        }
    }

    public Integer getDanger() {
        return danger;
    }

    public void setDanger(Integer danger) {
        this.danger = danger;
    }

    void toTableCriminal() throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO criminal (`id`, `profession`, `age`, `deadAge`, `sex`, `health`, `idMarriage`, `danger`)" + " VALUE (" + super.getId() + ", '" + super.getProfession() + "', " + super.getAge() + "," + super.getDeadAge() + ",'" + super.getSex() + "'," + super.getHealth() + "," + super.getIdMarriage() + "," + getDanger() + ");");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void doSmthBad() throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM criminal");
            while (rs.next()) {
                if ((int) (random() * 6) < 7) {
                    ResultSet rsall = stmt.executeQuery("SELECT id, profession, age, deadAge, sex, health, idMarriage  FROM `farmer` UNION  SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `police` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `criminal` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `doctor` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `civilian` ORDER BY id;");

                    rsall.close();
                }
            }
            rs.close();

        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }
}

//    SELECT column FROM table
//    ORDER BY RAND()
//LIMIT 1