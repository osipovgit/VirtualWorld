package com.osipov_evgeny.entity.roles;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Random;


@Entity
@Table(name = "farmers")
public class Farmer extends People {
    // TODO: метод для восполнения запасов еды (связать с временем года?)
    //       талант растет со временем?

    private Integer talent;

    public Farmer() {
        this.talent = 1 + new Random().nextInt(9);
    }

    public Farmer(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health) {
        super(userId, profession, deadAge, sex, health);
        this.talent = 1 + new Random().nextInt(9);
    }

    public Integer getTalent() {
        return talent;
    }
}
