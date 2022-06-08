package com.decursioteam.pickableorbs.datagen.utils;

import com.decursioteam.pickableorbs.datagen.Orbs;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;

public interface IOrbsData {

    JsonObject getRawOrbsData(String orb);

    Map<String, Orbs> getOrbs();

    Set<Orbs> getSetOfOrbs();

    Map<String, JsonObject> getRawOrbs();
}
