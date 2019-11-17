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
//                    switch ((int) random() * 5) {
                    switch (1) {
                        case 0:
                            ResultSet rrs0 = stmt.executeQuery("SELECT * FROM criminal");
//                            stmt.executeUpdate("UPDATE criminal SET deadAge = age WHERE profession = 'Criminal' AND deadAge <> age LIMIT 1");
                            rrs0.close();
                            break;
                        case 1:
                            ResultSet rrs1 = stmt.executeQuery("SELECT column FROM farmer " + " ORDER BY RAND() " + " LIMIT 1");
                            if (rs.getInt("danger") == 0) {
                                rrs1 = stmt.executeQuery("SELECT column FROM farmer ORDER BY RAND() LIMIT 1");
                                rrs1.deleteRow();
                            }
                            else {
                                rrs1.deleteRow();
                                rrs1.close();
                            }

                            break;
                        case 2:
                            ResultSet rrs2 = stmt.executeQuery("SELECT * FROM police");

                            rrs2.close();
                            break;
                        case 3:
                            ResultSet rrs3 = stmt.executeQuery("SELECT * FROM civilian");

                            rrs3.close();
                            break;
                        case 4:
                            ResultSet rrs4 = stmt.executeQuery("SELECT * FROM doctor");

                            rrs4.close();
                            break;
                    }
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