package com.decursioteam.healthorbs;

import com.decursioteam.healthorbs.core.HPClient;
import com.decursioteam.healthorbs.core.HealthPickupsConfig;
import com.decursioteam.healthorbs.core.entities.HalfHeartEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod("healthorbs")
@Mod.EventBusSubscriber(modid = HealthOrbs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HealthOrbs {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "healthorbs";

    private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    public static final RegistryObject<EntityType<HalfHeartEntity>> HALF_HEART_ENTITY = DEFERRED_REGISTER.register("normal_heart_pickup",
            () -> EntityType.Builder.<HalfHeartEntity>of(HalfHeartEntity::new, EntityClassification.MISC).sized(0.5f, 0.5f).build(MOD_ID + ":orb"));

    public HealthOrbs() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HPClient::register);
        DEFERRED_REGISTER.register(bus);
        MinecraftForge.EVENT_BUS.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::entityDropEvent);
        MinecraftForge.EVENT_BUS.addListener(this::blockBreakEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HealthPickupsConfig.COMMON_CONFIG);
    }

    private void setup(FMLCommonSetupEvent e) {
        ObfuscationReflectionHelper.setPrivateValue(EntityType.class, HALF_HEART_ENTITY.get(), EntityType.EXPERIENCE_ORB.getDescriptionId(), "field_210762_aX");
    }

    private void blockBreakEvent(BlockEvent.BreakEvent e){
        String blockName = e.getState().getBlock().getName().getString();
        BlockPos blockPos = e.getPos();
        World world = (World) e.getWorld();
    }

    private void entityDropEvent(LivingDeathEvent e){
        LivingEntity entity = e.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        List<? extends String> MOB_BLACKLIST = HealthPickupsConfig.COMMON.nhpmobList.get();
    }
}
