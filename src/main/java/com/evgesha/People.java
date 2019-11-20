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
    int generation = 5;

    void startGame() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtualworld?autoReconnect=true&useSSL=false", "root", "1236");
             Statement stat = connection.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS `farmer`");
            stat.execute("DROP TABLE IF EXISTS `police`");
            stat.execute("DROP TABLE IF EXISTS `doctor`");
            stat.execute("DROP TABLE IF EXISTS `criminal`");
            stat.execute("DROP TABLE IF EXISTS `civilian`");
            stat.executeUpdate("CREATE  TABLE `farmer`(`id` INT(3) NOT NULL, `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `talent` INT(2))");
            stat.executeUpdate("CREATE  TABLE `police`(`id` INT(3) NOT NULL, `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2))");
            stat.executeUpdate("CREATE  TABLE `doctor`(`id` INT(3) NOT NULL, `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `talent` INT(2))");
            stat.executeUpdate("CREATE  TABLE `criminal`(`id` INT(3) NOT NULL, `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2), `danger` INT(1))");
            stat.executeUpdate("CREATE  TABLE `civilian`(`id` INT(3) NOT NULL, `profession` VARCHAR (10), `age` INT(3), `deadAge` INT(3), `sex` VARCHAR(5), `health` INT(3), `idMarriage` INT(2))");
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void printAll() throws ClassNotFoundException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(connect, login, password);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, profession, age, sex, health, idMarriage  FROM `farmer` UNION  SELECT id, profession, age, sex, health, idMarriage FROM `police` UNION SELECT id, profession, age, sex, health, idMarriage FROM `criminal` UNION SELECT id, profession, age, sex, health, idMarriage FROM `doctor` UNION SELECT id, profession, age, sex, health, idMarriage FROM `civilian` ORDER BY id;");
            System.out.println("Id\tProfession\tAge\t\tSex\t\tHealth\tidMarriage");
            while (rs.next()) {
                System.out.printf("%-3s\t%-8s\t%-3s\t\t%-5s\t%-3s\t\t\t%-2s\n", rs.getInt("id"), rs.getString("profession"), rs.getInt("age"), rs.getString("sex"), rs.getInt("health"), rs.getInt("idMarriage"));
            }
            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void printGodMode() throws ClassNotFoundException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(connect, login, password);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, profession, age, deadAge, sex, health, idMarriage  FROM `farmer` UNION  SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `police` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `criminal` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `doctor` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `civilian` ORDER BY id;");
            System.out.println("Id\tProfession\tAge\t\tDeadAge\t\tSex\t\tHealth\tidMarriage");
            while (rs.next()) {
                System.out.printf("%-3s\t%-8s\t%-3s\t\t%-3s\t\t\t%-5s\t%-3s\t\t\t%-2s\n", rs.getInt("id"), rs.getString("profession"), rs.getInt("age"), rs.getInt("deadAge"), rs.getString("sex"), rs.getInt("health"), rs.getInt("idMarriage"));
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

    void doEat() throws ClassNotFoundException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(connect, login, password);
             Statement stmt = connection.createStatement(); Statement statement = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, profession, health  FROM `farmer` UNION  SELECT id, profession, health FROM `police` UNION SELECT id, profession, health FROM `criminal` UNION SELECT id, profession, health FROM `doctor` UNION SELECT id, profession, health FROM `civilian` ORDER BY id;");
            int[] idCase = new int[counterId];
            String[] tableCase = new String[counterId];
            int i = 0;
            while (rs.next()) {
                idCase[i] = rs.getInt("id");
                tableCase[i] = rs.getString("profession").toLowerCase();
                ++i;
            }
            rs.close();
            i = 0;
            ResultSet resultSet = stmt.executeQuery("SELECT id, profession, health  FROM `farmer` UNION  SELECT id, profession, health FROM `police` UNION SELECT id, profession, health FROM `criminal` UNION SELECT id, profession, health FROM `doctor` UNION SELECT id, profession, health FROM `civilian` ORDER BY id;");
            while (resultSet.next()) {
                int eat = 1 + (int) (random() * 3);
                if(food > 0) {
                    if(food < eat){
                        eat = food;
                    }
                    else food -= eat;
                } else eat = 1;
                switch (eat) {
                    case 1:
                        eat = -25;
                        break;
                    case 2:
                        eat = 5;
                        break;
                    case 3:
                        eat = 17;
                        break;
                }
//                System.out.println(idCase[i]);
                if (resultSet.getInt("health") + eat > 150){
                    statement.executeUpdate("UPDATE " + tableCase[i] + " SET health = 150 WHERE id = " + idCase[i]);
                } else {
                    statement.executeUpdate("UPDATE " + tableCase[i] + " SET health = health + " + eat + " WHERE id = " + idCase[i]);
                }
                ++i;
            }
            resultSet.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void marryMe() throws ClassNotFoundException {
        Class.forName(driver);
        try (Connection connection = DriverManager.getConnection(connect, login, password);
             Statement stmt = connection.createStatement(); Statement statement = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, profession, age, deadAge, sex, health, idMarriage  FROM `farmer` UNION  SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `police` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `criminal` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `doctor` UNION SELECT id, profession, age, deadAge, sex, health, idMarriage FROM `civilian` ORDER BY id;");
            int[] idCase = new int[counterId];
            String[] tableCase = new String[counterId];
            int i = 0;
            while (rs.next()) {
                idCase[i] = rs.getInt("id");
                tableCase[i] = rs.getString("profession").toLowerCase();
                ++i;
            }
            rs.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    void oneYearLater() throws ClassNotFoundException {
        Police police = new Police();
        Farmer farmer = new Farmer();
        Criminal criminal = new Criminal();
        Doctor doctor = new Doctor();
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

        police.catchTheCriminal(counterId);
        food = farmer.generationFood(food);
        doEat();
        criminal.doSmthBad(counterId);                                                                                  //criminal work
        doctor.healthUp(counterId);                                                                                     //doctors work
//        marryMe();

        createGeneration(generation);
        generation = 0;
        try (Connection connection = DriverManager.getConnection(connect, login, password);                             //kill if deadAge == age
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM civilian WHERE `deadAge` = `age` OR `health` < 1");
            stmt.executeUpdate("DELETE FROM criminal WHERE `deadAge` = `age` OR `health` < 1");
            stmt.executeUpdate("DELETE FROM police WHERE `deadAge` = `age` OR `health` < 1");
            stmt.executeUpdate("DELETE FROM farmer WHERE `deadAge` = `age` OR `health` < 1");
            stmt.executeUpdate("DELETE FROM doctor WHERE `deadAge` = `age` OR `health` < 1");

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
