package com.evgesha;

import java.sql.*;

import static java.lang.Math.random;

public class Police extends People {
    public Police() {
    }

    public Police(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
        this.setId(id);
        this.setProfession(profession);
        this.setAge(age);
        this.setDeadAge(deadAge);
        this.setSex(sex);
        this.setHealth(health);
        this.setIdMarriage(idMarriage);
    }

    void toTablePolice() throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO police (`id`, `profession`, `age`, `deadAge`, `sex`, `health`, `idMarriage`)" + " VALUE (" + super.getId() + ", '" + super.getProfession() + "', " + super.getAge() + "," + super.getDeadAge() + ",'" + super.getSex() + "'," + super.getHealth() + "," + super.getIdMarriage() + ");");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void catchTheCriminal(int idid) throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement(); Statement statement = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM criminal");
            int col = -1;
            int[] idCase = new int[idid];
            while (rs.next()) {
                idCase[++col] = rs.getInt("id");
            }
            rs = stmt.executeQuery("SELECT * FROM `police`");
            int chance = 0;
            while (rs.next()) {
                ++chance;
            }
            if ((int) (random() * 10) < chance && col != -1) {
                int rnd = (int) (random() * col);
                stmt.executeUpdate("UPDATE criminal SET deadAge = age WHERE id = " + idCase[rnd] + " AND deadAge <> age LIMIT 1");
                System.out.println("[" + idCase[rnd] + "] Criminal was caught by " + chance + " policeman");
            }
            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }
}
