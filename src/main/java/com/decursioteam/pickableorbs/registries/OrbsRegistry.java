package com.decursioteam.pickableorbs.registries;

import com.decursioteam.pickableorbs.PickableOrbs;
import com.decursioteam.pickableorbs.entities.HalfHeartEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrbsRegistry {

    public static DeferredRegister<EntityType<?>> ORB_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, PickableOrbs.MOD_ID);
    private static final Map<String, EntityType<? extends HalfHeartEntity>> ORBS = new HashMap<>();

    public static Set<EntityType<? extends HalfHeartEntity>> getOrbsHashSet() {
        return new HashSet<>(ORBS.values());
    }
    public static Map<String, EntityType<? extends HalfHeartEntity>> getOrbs() {
        return ORBS;
    }
}
