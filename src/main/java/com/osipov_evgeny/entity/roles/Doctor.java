package com.osipov_evgeny.entity.roles;

import javax.persistence.*;

import java.util.Random;

@Entity
@Table(name = "doctors")
public class Doctor extends People {
    private Integer talent;

    public Doctor() {
        this.talent = 1 + new Random().nextInt(10);
    }

    public Doctor(Long userId, InnateTalent profession, Integer deadAge, String sex,
                  Integer health) {
        super(userId, profession, deadAge, sex, health);
        this.talent = 1 + new Random().nextInt(10);
    }

    public Integer getTalent() {
        return talent;
    }

    // TODO: метод для лечения
}
