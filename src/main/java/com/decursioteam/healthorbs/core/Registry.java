package com.decursioteam.healthorbs.core;

import com.decursioteam.healthorbs.HealthOrbs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final SoundEvent GET_HEART_SOUND = new SoundEvent(new ResourceLocation(HealthOrbs.MOD_ID, "get_heart_sound"))
            .setRegistryName("get_heart_sound");

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> soundEvents = event.getRegistry();
        soundEvents.register(GET_HEART_SOUND);
    }
}
