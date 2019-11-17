package com.evgesha;

import java.sql.*;

import static java.lang.Math.random;

public class People {
    private Integer id;
    private String profession;
    private Integer age;
    private Integer deadAge;
    private String sex;
    private Integer health;
    private Integer idMarriage;
    private String connect = "jdbc:mysql://localhost:3306/virtualworld?autoReconnect=true&useSSL=false";
    private String login = "root";
    private String password = "1236";
    private String driver = "com.mysql.jdbc.Driver";
    private Integer counterId = 0;
    int food = 45;

    void startGame() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtualworld?autoReconnect=true&useSSL=false", "root", "1236");
             Statement stat = connection.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS `farmer`");
            stat.execute("DROP TABLE IF EXISTS `police`");
            stat.execute("DROP TABLE IF EXISTS `doctor`");
            stat.execute("DROP TABLE IF EXISTS `criminal`");
            stat.execute("DROP TABLE IF EXISTS `civilian`");
            stat.executeUpdate("CREATE  TABLE `farmer`(`id` INT(3), `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `talent` INT(2))");
            stat.executeUpdate("CREATE  TABLE `police`(`id` INT(3), `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2))");
            stat.executeUpdate("CREATE  TABLE `doctor`(`id` INT(3), `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `talent` INT(2))");
            stat.executeUpdate("CREATE  TABLE `criminal`(`id` INT(3), `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `danger` INT(1))");
            stat.executeUpdate("CREATE  TABLE `civilian`(`id` INT(3), `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2))");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void printAll() throws ClassNotFoundException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(connect, login, password);
             Statement stmt = connection.createStatement()) {
//            ResultSet rs = stmt.executeQuery("SELECT * FROM `farmer` JOIN `police` ON farmer.id=police.id JOIN `doctor` ON police.id=doctor.id JOIN `criminal` ON doctor.id=criminal.id JOIN `civilian` ON criminal.id=civilian.id");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM civilian");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM criminal");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM police");
            ResultSet rs = stmt.executeQuery("SELECT * FROM farmer");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM doctor");
//            ResultSet rs = stmt.executeQuery("SELECT 1,2,3,4,5,6,7 FROM" + "(People)");
            System.out.println("Id\tProfession\tAge\t\tDeadAge\t\tSex\t\tHealth\tidMarriage");
            while (rs.next()) {
                System.out.printf("%-3s\t%-8s\t%-3s\t\t%-3s\t\t\t%-5s\t%-3s\t\t%-2s\n", rs.getInt("id"), rs.getString("profession"), rs.getInt("age"), rs.getInt("deadAge"), rs.getString("sex"), rs.getInt("health"), rs.getInt("idMarriage"));
            }
            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void createGeneration(int n) throws ClassNotFoundException {
        String[] prof = {"Civilian", "Farmer", "Civilian", "Police", "Civilian", "Doctor", "Civilian", "Criminal"};
        String[] sexxx = {"man", "woman"};
        while (n > 0) {
            --n;
            ++counterId;
            People entity = new People(counterId, "determined...", 0, (int) (random() * (101)), "(wo)man", (int) ((random() * (101)) + 50), 0);
            entity.setProfession(prof[(int) (random() * 8)]);
            entity.setSex(sexxx[(int) (random() * 2)]);
            switch (entity.getProfession()) {
                case "Farmer":
                    Farmer farmer = new Farmer(entity.id, entity.profession, entity.age, entity.deadAge, entity.sex, entity.health, entity.idMarriage);
                    farmer.toTableFarmer();
                    break;
                case "Civilian":
                    Civilian civilian = new Civilian(entity.id, entity.profession, entity.age, entity.deadAge, entity.sex, entity.health, entity.idMarriage);
                    civilian.toTableCivilian();
                    break;
                case "Police":
                    Police police = new Police(entity.id, entity.profession, entity.age, entity.deadAge, entity.sex, entity.health, entity.idMarriage);
                    police.toTablePolice();
                    break;
                case "Doctor":
                    Doctor doctor = new Doctor(entity.id, entity.profession, entity.age, entity.deadAge, entity.sex, entity.health, entity.idMarriage);
                    doctor.toTableDoctor();
                    break;
                case "Criminal":
                    Criminal criminal = new Criminal(entity.id, entity.profession, entity.age, entity.deadAge, entity.sex, entity.health, entity.idMarriage);
                    criminal.toTableCriminal();
                    break;
            }
        }
    }

    void oneYearLater() throws ClassNotFoundException {
        Police police = new Police();
        Farmer farmer = new Farmer();
        Criminal criminal = new Criminal();
        Class.forName(driver);

        try (Connection connection = DriverManager.getConnection(connect, login, password);                             //++age for all
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("UPDATE civilian SET `age` = `age` + 1");
            stmt.executeUpdate("UPDATE criminal SET `age` = `age` + 1");
            stmt.executeUpdate("UPDATE police SET `age` = `age` + 1");
            stmt.executeUpdate("UPDATE farmer SET `age` = `age` + 1");
            stmt.executeUpdate("UPDATE doctor SET `age` = `age` + 1");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

        police.catchTheCriminal();
        food = farmer.generationFood(food);
        //random food +-hp
//        criminal.doSmthBad();                                                                                           //criminal work
        //doctors work
        //marriage and have sex
        createGeneration(5);

        try (Connection connection = DriverManager.getConnection(connect, login, password);                             //kill if deadAge == age
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM civilian WHERE `deadAge` = `age`");
            stmt.executeUpdate("DELETE FROM criminal WHERE `deadAge` = `age`");
            stmt.executeUpdate("DELETE FROM police WHERE `deadAge` = `age`");
            stmt.executeUpdate("DELETE FROM farmer WHERE `deadAge` = `age`");
            stmt.executeUpdate("DELETE FROM doctor WHERE `deadAge` = `age`");

        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    public People() {
    }

    public People(Integer id, String profession, Integer age, Integer deadAge, String sex, Integer health, Integer idMarriage) {
        this.id = id;
        this.profession = profession;
        this.age = age;
        this.deadAge = deadAge;
        this.sex = sex;
        this.health = health;
        this.idMarriage = idMarriage;
    }

    public Integer getId() {
        return id;
    }

    public String getProfession() {
        return profession;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getDeadAge() {
        return deadAge;
    }

    public String getSex() {
        return sex;
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getIdMarriage() {
        return idMarriage;
    }

    public Integer getCounterId() {
        return counterId;
    }

    public String getConnect() {
        return connect;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDeadAge(Integer deadAge) {
        this.deadAge = deadAge;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setIdMarriage(Integer idMarriage) {
        this.idMarriage = idMarriage;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }
}
