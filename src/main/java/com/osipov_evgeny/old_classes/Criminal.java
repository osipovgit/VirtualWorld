package com.osipov_evgeny.old_classes;

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

    void doSmthBad(int idid) throws ClassNotFoundException {
        Class.forName(getDriver());
        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
             Statement stmt = connection.createStatement(); Statement statement = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, profession, health  FROM `farmer` UNION  SELECT id, profession, health FROM `police` UNION SELECT id, profession, health FROM `criminal` UNION SELECT id, profession, health FROM `doctor` UNION SELECT id, profession, health FROM `civilian` ORDER BY id;");
            int[] idCase = new int[idid];
            String[] tableCase = new String[idid];
            int i = 0;
            while (rs.next()) {
                idCase[i] = rs.getInt("id");
                tableCase[i] = rs.getString("profession").toLowerCase();
                ++i;
            }
            rs = stmt.executeQuery("SELECT * FROM `criminal`");
            while (rs.next()) {
                if ((int) (random() * 8) > 6) {
                    int bad = (int) (random() * i);
                    if (rs.getInt("danger") == 1 && rs.getInt("id") != idCase[bad]) {
                        statement.executeUpdate("UPDATE " + tableCase[bad] + " SET `deadAge` = `age` WHERE id = " + idCase[bad]);
                        System.out.println("[" + idCase[bad] + "] " + tableCase[bad] + " was killed by [" + rs.getInt("id") + "] Criminal");
                    } else if (rs.getInt("id") != idCase[bad]) {
                        statement.executeUpdate("UPDATE " + tableCase[bad] + " SET health = health - 40 WHERE id = " + idCase[bad]);
                        System.out.println("[" + idCase[bad] + "] " + tableCase[bad] + " was damaged by [" + rs.getInt("id") + "] Criminal");
                    }
                }
            }
            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }
}