package com.decursioteam.pickableorbs;

import com.decursioteam.pickableorbs.client.HPClient;
import com.decursioteam.pickableorbs.config.CommonConfig;
import com.decursioteam.pickableorbs.config.Readme;
import com.decursioteam.pickableorbs.datagen.OrbsData;
import com.decursioteam.pickableorbs.entities.HalfHeartEntity;
import com.decursioteam.pickableorbs.registries.OrbsRegistry;
import com.decursioteam.pickableorbs.registries.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod("pickableorbs")
@Mod.EventBusSubscriber(modid = PickableOrbs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PickableOrbs {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "pickableorbs";

    public PickableOrbs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.config, "pickableorbs/common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Readme.config, "pickableorbs/readme.toml");
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Registry.setupOrbs();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HPClient::register);
        }

        MinecraftForge.EVENT_BUS.addListener(this::entityDropEvent);
        MinecraftForge.EVENT_BUS.addListener(this::blockBreakEvent);

        CommonConfig.loadConfig(CommonConfig.config, FMLPaths.CONFIGDIR.get().resolve("pickableorbs/common.toml").toString());

        if (CommonConfig.generate_defaults.get()) {
            Registry.setupDefaultOrbs();
            CommonConfig.generate_defaults.set(false);
            CommonConfig.generate_defaults.save();
        }

        OrbsRegistry.ORB_TYPES.register(bus);
        Registry.registerOrbTypes();
    }

    private void blockBreakEvent(BlockEvent.BreakEvent e){
        Level world = (Level) e.getWorld();
        OrbsRegistry.getOrbs().forEach((s, entityType) ->{
            List<ResourceLocation> blockSet = OrbsData.getOrbData(s).getData().getBlockSet();
            String blockListType = OrbsData.getOrbData(s).getData().getBlockListType();
            double blockDropChance = OrbsData.getOrbData(s).getData().getBlockDropChance();
            Random random = new Random();
            if(blockListType.equalsIgnoreCase("whitelist")) {
                if (blockSet.contains(e.getState().getBlock().getRegistryName())) {
                    if (blockDropChance != 0.0 && blockDropChance >= random.nextDouble() * 100) {
                        // For debugging
                        // e.getPlayer().displayClientMessage(new StringTextComponent(e.getPos().getX() + " " + e.getPos().getY() + " " + e.getPos().getZ()), true);
                        world.addFreshEntity(new HalfHeartEntity((EntityType<HalfHeartEntity>) entityType, world, e.getPos().getX(), e.getPos().getY(),
                                e.getPos().getZ(), s, OrbsData.getOrbData(s)));
                    }
                }
            }
            else if(blockListType.equalsIgnoreCase("blacklist")){
                if (!blockSet.contains(e.getState().getBlock().getRegistryName())) {
                    if (blockDropChance != 0.0 && blockDropChance >= random.nextDouble() * 100) {
                        // For debugging
                        // e.getPlayer().displayClientMessage(new StringTextComponent(e.getPos().getX() + " " + e.getPos().getY() + " " + e.getPos().getZ()), true);
                        world.addFreshEntity(new HalfHeartEntity((EntityType<HalfHeartEntity>) entityType, world, e.getPos().getX(), e.getPos().getY(),
                                e.getPos().getZ(), s, OrbsData.getOrbData(s)));
                    }
                }
            }
        });
    }

    private void entityDropEvent(LivingDeathEvent e){
        LivingEntity entity = e.getEntityLiving();
        Level world = entity.getCommandSenderWorld();
        Entity sourceEntity = e.getSource().getEntity();
        if(sourceEntity instanceof Player) {
            OrbsRegistry.getOrbs().forEach((s, entityType) -> {
                List<ResourceLocation> entitySet = OrbsData.getOrbData(s).getData().getEntitySet();
                String entityListType = OrbsData.getOrbData(s).getData().getEntityListType();
                double entityDropChance = OrbsData.getOrbData(s).getData().getEntityDropChance();
                Random random = new Random();
                if(entityListType.equalsIgnoreCase("whitelist")){
                    if (entitySet.contains(entity.getType().getRegistryName())) {
                        if (entityDropChance != 0.0 && entityDropChance >= random.nextDouble()*100) {
                            // For debugging
                            // playerEntity.displayClientMessage(new TextComponent(entity.getX() + " " + entity.getY() + " " + entity.getZ()), true);
                            world.addFreshEntity(new HalfHeartEntity((EntityType<HalfHeartEntity>) entityType, world, entity.getX(), entity.getY(),
                                    entity.getZ(), s, OrbsData.getOrbData(s)));
                        }
                    }
                }
                else if(entityListType.equalsIgnoreCase("blacklist")){
                    if (!entitySet.contains(entity.getType().getRegistryName())) {
                        if (entityDropChance != 0.0 && entityDropChance >= random.nextDouble()*100) {
                            // For debugging
                            // playerEntity.displayClientMessage(new TextComponent(entity.getX() + " " + entity.getY() + " " + entity.getZ()), true);
                            world.addFreshEntity(new HalfHeartEntity((EntityType<HalfHeartEntity>) entityType, world, entity.getX(), entity.getY(),
                                    entity.getZ(), s, OrbsData.getOrbData(s)));
                        }
                    }
                }
            });
        }
    }
}
