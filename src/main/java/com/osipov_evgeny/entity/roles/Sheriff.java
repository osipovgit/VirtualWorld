package com.osipov_evgeny.entity.roles;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sheriffs")
public class Sheriff extends People {
    public Sheriff() {
    }

    public Sheriff(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health) {
        super(userId, profession, deadAge, sex, health);
    }

    // TODO: метод для поиска убийц
}
