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

    void catchTheCriminal () throws ClassNotFoundException {
        if ((int) (random() * 10) > 8) {
            Class.forName(getDriver());
            try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
                 Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM criminal");
                int col = 0;
                while (rs.next()) {
                    ++col;
                }
                if (col != 0) {
                    stmt.executeUpdate("UPDATE criminal SET deadAge = age WHERE profession = 'Criminal' AND deadAge <> age LIMIT 1");
                }
                rs.close();
            } catch (SQLException err) {
                System.err.println(err.getMessage());
            }
        }
    }
}
