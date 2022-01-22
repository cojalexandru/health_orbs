package com.decursioteam.healthorbs.core;

import com.decursioteam.healthorbs.HealthOrbs;
import com.decursioteam.healthorbs.core.renderers.HalfHeartRenderer;
import com.decursioteam.healthorbs.core.renderers.RareHalfHeartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class HPClient {
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(HPClient::setupClient);
    }

    protected static void setupClient(FMLClientSetupEvent event) {
        EntityRenderers.register(HealthOrbs.HALF_HEART_ENTITY.get(), HalfHeartRenderer::new);
        EntityRenderers.register(HealthOrbs.RARE_HALF_HEART_ENTITY.get(), RareHalfHeartRenderer::new);
    }
}