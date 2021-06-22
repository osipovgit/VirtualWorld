package com.osipov.evgeny.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum ModelState {
    VILLAGER,
    FARMER,
    DOCTOR,
    SHERIFF,
    CRIMINAL,
    PRISONER,
    CAUGHT,
    DEAD;

    public static ModelState getRandomState() {
        List<ModelState> stateList = new ArrayList<>(Arrays.asList(VILLAGER, FARMER, DOCTOR, SHERIFF, CRIMINAL));
        return stateList.get(new Random().nextInt(stateList.size()));
    }
}
