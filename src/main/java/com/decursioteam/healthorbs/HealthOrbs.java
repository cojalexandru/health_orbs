package com.decursioteam.healthorbs;

import com.decursioteam.healthorbs.core.HPClient;
import com.decursioteam.healthorbs.core.HealthPickupsConfig;
import com.decursioteam.healthorbs.core.entities.HalfHeartEntity;
import com.decursioteam.healthorbs.core.entities.RareHalfHeartEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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
            () -> EntityType.Builder.<HalfHeartEntity>of(HalfHeartEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(MOD_ID + ":normal_heart_pickup"));
    public static final RegistryObject<EntityType<RareHalfHeartEntity>> RARE_HALF_HEART_ENTITY = DEFERRED_REGISTER.register("super_heart_pickup",
            () -> EntityType.Builder.<RareHalfHeartEntity>of(RareHalfHeartEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(MOD_ID + ":super_heart_pickup"));

    public HealthOrbs() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HPClient::register);
        DEFERRED_REGISTER.register(bus);
        MinecraftForge.EVENT_BUS.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::entityDropEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HealthPickupsConfig.COMMON_CONFIG);
    }

    private void setup(FMLCommonSetupEvent e) {
        ObfuscationReflectionHelper.setPrivateValue(EntityType.class, RARE_HALF_HEART_ENTITY.get(), EntityType.EXPERIENCE_ORB.getDescriptionId(), "field_210762_aX");
        ObfuscationReflectionHelper.setPrivateValue(EntityType.class, HALF_HEART_ENTITY.get(), EntityType.EXPERIENCE_ORB.getDescriptionId(), "field_210762_aX");
    }

    private void entityDropEvent(LivingDeathEvent e){
        LivingEntity entity = e.getEntityLiving();
        Level world = entity.getCommandSenderWorld();
        List<? extends String> MOB_BLACKLIST = HealthPickupsConfig.COMMON.nhpmobList.get();
        List<? extends String> MOB_BLACKLIST_2 = HealthPickupsConfig.COMMON.rhpmobList.get();
        if((e.getSource().getEntity() instanceof Player) && !(e.getEntity() instanceof Player)) {
            if (Math.floor(Math.random() * 100) + 1 <= HealthPickupsConfig.COMMON.nhpChance.get()
                    && HealthPickupsConfig.COMMON.nhpChance.get() != 0) {
                if (HealthPickupsConfig.COMMON.nhpWhiteList.get()) {
                    if (MOB_BLACKLIST.contains(entity.getName().getString()))
                        world.addFreshEntity(new HalfHeartEntity(world, entity.getX(), entity.getY(), entity.getZ()));
                } else if (!MOB_BLACKLIST.contains(entity.getName().getString()))
                    world.addFreshEntity(new HalfHeartEntity(world, entity.getX(), entity.getY(), entity.getZ()));
            }
            if (Math.floor(Math.random() * 100) + 1 <= HealthPickupsConfig.COMMON.rhpChance.get()
                    && HealthPickupsConfig.COMMON.rhpChance.get() != 0) {
                if (HealthPickupsConfig.COMMON.rhpWhiteList.get()) {
                    if (MOB_BLACKLIST_2.contains(entity.getName().getString()))
                        world.addFreshEntity(new RareHalfHeartEntity(world, entity.getX(), entity.getY(), entity.getZ()));
                } else if (!MOB_BLACKLIST_2.contains(entity.getName().getString()))
                    world.addFreshEntity(new RareHalfHeartEntity(world, entity.getX(), entity.getY(), entity.getZ()));
            }
        }
    }
}
