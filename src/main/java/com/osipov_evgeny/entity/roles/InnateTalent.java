package com.osipov_evgeny.entity.roles;

import java.util.Random;

public enum InnateTalent {
    CIVILIAN,
    DOCTOR,
    FARMER,
    MAFIA,
    SHERIFF;

    public InnateTalent getRandomRole() {
        return InnateTalent.values()[new Random().nextInt(InnateTalent.values().length)];
    }
}
