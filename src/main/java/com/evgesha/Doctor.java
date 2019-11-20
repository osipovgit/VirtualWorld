package com.evgesha;

import java.sql.*;

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

    void healthUp(int idid) throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement(); Statement statement = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, talent  FROM `doctor`");
            int[] talentDoc = new int[idid];
            int i = 0;
            while (rs.next()) {
                talentDoc[i] = rs.getInt("talent") * 2;
                ++i;
            }
            rs = stmt.executeQuery("SELECT id, profession, health  FROM `farmer` UNION  SELECT id, profession, health FROM `police` UNION SELECT id, profession, health FROM `criminal` UNION SELECT id, profession, health FROM `doctor` UNION SELECT id, profession, health FROM `civilian` ORDER BY id;");
            while (rs.next()) {
                int rnd = (int) (random() * i);
                if (rs.getInt("id") != rnd + 1){
                    if (rs.getInt("health") + talentDoc[rnd] > 150){
                        statement.executeUpdate("UPDATE " + rs.getString("profession").toLowerCase() + " SET health = 150 WHERE id = " + rs.getInt("id"));
                    } else {
                        statement.executeUpdate("UPDATE " + rs.getString("profession").toLowerCase() + " SET health = health +" + talentDoc[rnd] + " WHERE id = " + rs.getInt("id"));
                    }
                }
            }


            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

    }
}
