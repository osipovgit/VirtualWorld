package com.osipov_evgeny.entity.roles;

import javax.persistence.*;

@MappedSuperclass
public abstract class People {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private InnateTalent profession;
    private Integer age;
    private Integer deadAge;
    private String sex;
    private Integer health;
    private Integer idMarriage;

    public People() {
    }

    public People(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health) {
        this.userId = userId;
        this.profession = profession;
        this.age = 0;
        this.deadAge = deadAge;
        this.sex = sex;
        this.health = health;
        this.idMarriage = null;
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public InnateTalent getProfession() {
        return profession;
    }

    public void setProfession(InnateTalent profession) {
        this.profession = profession;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDeadAge() {
        return deadAge;
    }

    public void setDeadAge(Integer deadAge) {
        this.deadAge = deadAge;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getIdMarriage() {
        return idMarriage;
    }

    public void setIdMarriage(Integer idMarriage) {
        this.idMarriage = idMarriage;
    }
}
