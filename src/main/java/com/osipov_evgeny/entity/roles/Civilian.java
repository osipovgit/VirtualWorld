package com.osipov_evgeny.entity.roles;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "civilians")
public class Civilian extends People {
    // TODO: добавить влияние на ход игры

    public Civilian() {
    }

    public Civilian(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health, Integer talent) {
        super(userId, profession, deadAge, sex, health);
    }
}
