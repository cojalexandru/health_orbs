package com.decursioteam.healthorbs.core;

import com.decursioteam.healthorbs.HealthOrbs;
import com.decursioteam.healthorbs.core.renderers.HalfHeartRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class HPClient {
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(HPClient::setupClient);
    }

    protected static void setupClient(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(HealthOrbs.HALF_HEART_ENTITY.get(), HalfHeartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HealthOrbs.RARE_HALF_HEART_ENTITY.get(), RareHalfHeartRenderer::new);
    }
}