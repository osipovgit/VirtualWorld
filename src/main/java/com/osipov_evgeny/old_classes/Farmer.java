//package com.osipov_evgeny.old_classes;
//
//import java.sql.*;
//
//import static java.lang.Math.random;
//
//public class Farmer extends People {
//    private Integer talent = 0;
//    int food = 45;
//
//    public Farmer() {
//    }
//
//    public Farmer(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
//        this.setId(id);
//        this.setProfession(profession);
//        this.setAge(age);
//        this.setDeadAge(deadAge);
//        this.setSex(sex);
//        this.setHealth(health);
//        this.setIdMarriage(idMarriage);
//        this.talent = 1 + (int) (random() * 9);
//    }
//
//    public Integer getTalent() {
//        return talent;
//    }
//
//    public void setTalent(Integer talent) {
//        this.talent = talent;
//    }
//
//    void toTableFarmer() throws ClassNotFoundException {
//        Class.forName(getDriver());
//        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
//             Statement stmt = connection.createStatement()) {
//            stmt.executeUpdate("INSERT INTO farmer (`id`, `profession`, `age`, `deadAge`, `sex`, `health`, `idMarriage`, `talent`)" + " VALUE (" + super.getId() + ", '" + super.getProfession() + "', " + super.getAge() + "," + super.getDeadAge() + ",'" + super.getSex() + "'," + super.getHealth() + "," + super.getIdMarriage() + "," + getTalent() + ");");
//        } catch (SQLException err) {
//            System.err.println(err.getMessage());
//        }
//    }
//
//    int generationFood(int food) throws ClassNotFoundException {
//        Class.forName(getDriver());
//        try (Connection connection = DriverManager.getConnection(getConnect(), getLogin(), getPassword());
//             Statement stmt = connection.createStatement()) {
//            ResultSet rs = stmt.executeQuery("SELECT * FROM farmer");
//            while (rs.next()) {
//                if (rs.getInt("talent") <= 3) {
//                    food += 6;
//                } else if (rs.getInt("talent") >= 7) {
//                    food += 12;
//                } else {
//                    food += 9;
//                }
//            }
//        } catch (SQLException err) {
//            System.err.println(err.getMessage());
//        }
//        return food;
//    }
//}
///*
//1-3: 6
//4-6: 9
//7-9: 12
//-------------
//1:  - 15
//2:  + 5
//3:  + 15
// */