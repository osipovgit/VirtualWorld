package com.osipov_evgeny.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum InnateTalent {
    VILLAGER,
    FARMER,
    DOCTOR,
    SHERIFF,
    CRIMINAL,
    PRISONER,
    CAUGHT,
    DEAD;

    public static InnateTalent getRandomRole() {
        return new ArrayList<>(Arrays.asList(VILLAGER, FARMER, DOCTOR, SHERIFF, CRIMINAL))
                .get(new Random().nextInt(InnateTalent.values().length - 3));
    }
}
