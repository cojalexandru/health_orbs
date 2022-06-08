package com.decursioteam.pickableorbs.registries;

import com.decursioteam.pickableorbs.PickableOrbs;
import com.decursioteam.pickableorbs.config.CommonConfig;
import com.decursioteam.pickableorbs.datagen.OrbsData;
import com.decursioteam.pickableorbs.datagen.utils.FileUtils;
import com.decursioteam.pickableorbs.entities.HalfHeartEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final Gson GSON = new Gson();
    public static final SoundEvent GET_HEART_SOUND = new SoundEvent(new ResourceLocation(PickableOrbs.MOD_ID, "get_heart_sound"))
            .setRegistryName("get_heart_sound");

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> soundEvents = event.getRegistry();
        soundEvents.register(GET_HEART_SOUND);
    }

    public static void registerOrbTypes(){
       OrbsData.getRegistry().getOrbs().forEach((name, data) -> registerOrb(name));
    }

    public static void registerOrb(String name) {
        final EntityType<? extends HalfHeartEntity> orbType = EntityType.Builder
                .<HalfHeartEntity>of((type, world) -> new HalfHeartEntity(type, world, name), MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .build(name + "_orb");
        OrbsRegistry.ORB_TYPES.register(name + "_orb", () -> orbType);
        OrbsRegistry.getOrbs().put(name, orbType);
    }

    public static void setupDefaultOrbs() {
        if (CommonConfig.generate_defaults.get()) {
            FileUtils.setupDefaultFiles("/data/pickableorbs/default_orbs", createCustomPath("orbs"));
        }
        PickableOrbs.LOGGER.info("Loading Orbs...");
        FileUtils.streamFilesAndParse(createCustomPath("orbs"), Registry::parseOrb, "Could not stream orbs!");

        OrbsData.getRegistry().regenerateCustomOrbsData();
    }
    public static void setupOrbs() {
        PickableOrbs.LOGGER.info("Loading Orbs...");
        FileUtils.streamFilesAndParse(createCustomPath("orbs"), Registry::parseOrb, "Could not stream orbs!");

        OrbsData.getRegistry().regenerateCustomOrbsData();
    }

    private static void parseOrb(Reader reader, String name) {
        JsonObject jsonObject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
        name = Codec.STRING.fieldOf("name").orElse(name).codec().fieldOf("OrbData").codec().parse(JsonOps.INSTANCE, jsonObject).get().orThrow();
        OrbsData.getRegistry().cacheRawOrbsData(name.toLowerCase(Locale.ENGLISH).replace(" ", "_"), jsonObject);
    }

    private static Path createCustomPath(String pathName) {
        Path customPath = Paths.get(FMLPaths.CONFIGDIR.get().toAbsolutePath().toString(), PickableOrbs.MOD_ID, pathName);
        createDirectory(customPath, pathName);
        return customPath;
    }


    private static void createDirectory(Path path, String dirName) {
        try { Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { PickableOrbs.LOGGER.error("failed to create \"{}\" directory", dirName);}
    }
}
