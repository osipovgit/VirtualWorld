package com.osipov_evgeny.entity;

import java.util.Random;

public enum InnateTalent {
    CIVILIAN,
    DOCTOR,
    FARMER,
    MAFIA,
    SHERIFF;

    public static InnateTalent getRandomRole() {
        return InnateTalent.values()[new Random().nextInt(InnateTalent.values().length)];
    }
}
