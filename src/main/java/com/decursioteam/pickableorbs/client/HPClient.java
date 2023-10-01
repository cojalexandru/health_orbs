package com.decursioteam.pickableorbs.client;

import com.decursioteam.pickableorbs.datagen.OrbsData;
import com.decursioteam.pickableorbs.registries.OrbsRegistry;
import com.decursioteam.pickableorbs.renderers.HalfHeartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class HPClient {
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(HPClient::setupClient);
    }

    protected static void setupClient(FMLClientSetupEvent event) {
        OrbsRegistry.getOrbs().forEach((s, entityType) ->
                EntityRenderers.register(entityType.get(),
                        manager -> new HalfHeartRenderer(manager, OrbsData.getOrbData(s).getData(), OrbsData.getOrbData(s).getExtraData())));
    }
}