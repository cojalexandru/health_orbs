package com.decursioteam.pickableorbs.datagen;

import com.decursioteam.pickableorbs.PickableOrbs;
import com.decursioteam.pickableorbs.datagen.utils.IOrbsData;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class OrbsData implements IOrbsData {

    private static final OrbsData INSTANCE = new OrbsData();
    private static final Map<String, JsonObject> RAW_DATA = new LinkedHashMap<>();
    private static final Map<String, com.decursioteam.pickableorbs.datagen.Orbs> CUSTOM_DATA = new LinkedHashMap<>();


    public static OrbsData getRegistry() {
        return INSTANCE;
    }

    public static com.decursioteam.pickableorbs.datagen.Orbs getOrbData(ResourceLocation orbType) {
        return CUSTOM_DATA.getOrDefault(orbType.getPath().replaceAll("_orb$", ""), com.decursioteam.pickableorbs.datagen.Orbs.DEFAULT);
    }

    public static com.decursioteam.pickableorbs.datagen.Orbs getOrbData(String orbType) {
        return CUSTOM_DATA.getOrDefault(orbType, com.decursioteam.pickableorbs.datagen.Orbs.DEFAULT);
    }

    public void regenerateCustomOrbsData() {
        RAW_DATA.forEach((s, jsonObject) -> CUSTOM_DATA.compute(s, (s1, orbData) ->
                com.decursioteam.pickableorbs.datagen.Orbs.codec(s).parse(JsonOps.INSTANCE, jsonObject)
                        .getOrThrow(false, s2 -> PickableOrbs.LOGGER.error("Couldn't create data for {} orb!", s))));
    }

    @Override
    public JsonObject getRawOrbsData(String orb) {
        return RAW_DATA.get(orb);
    }

    public void cacheRawOrbsData(String orbType, JsonObject orbData) {
        RAW_DATA.computeIfAbsent(orbType.toLowerCase(Locale.ENGLISH).replace(" ", "_"), s -> Objects.requireNonNull(orbData));
    }

    public Map<String, JsonObject> getRawOrbs() {
        return Collections.unmodifiableMap(RAW_DATA);
    }


    public Map<String, com.decursioteam.pickableorbs.datagen.Orbs> getOrbs() {
        return Collections.unmodifiableMap(CUSTOM_DATA);
    }

    public Set<com.decursioteam.pickableorbs.datagen.Orbs> getSetOfOrbs() {
        return Collections.unmodifiableSet(new HashSet<>(CUSTOM_DATA.values()));
    }
}
