package com.osipov_evgeny.entity.roles;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Random;

@Entity
@Table(name = "mafias")
public class Mafia extends People{
    private Integer danger;

    public Mafia() {
        this.danger = (new Random().nextInt(10) < 9 ? 0 : 1);
    }

    public Mafia(Long userId, InnateTalent profession, Integer deadAge, String sex, Integer health) {
        super(userId, profession, deadAge, sex, health);
        this.danger = (new Random().nextInt(10) < 9 ? 0 : 1);
    }

    public Integer getDanger() {
        return danger;
    }

    // TODO: метод для убийства/урона
}
