package com.decursioteam.healthorbs.core;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class HealthPickupsConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static final Config COMMON = new Config();
    
    public enum MobBlacklist {
        entityname;
    }
    public static final class Config {
        // Global
        public final ForgeConfigSpec.BooleanValue healthPickupSound;
        // Normal Health Pickup
        public final ForgeConfigSpec.IntValue nhpChance;
        public final ForgeConfigSpec.IntValue nhpPickupDelay;
        public final ForgeConfigSpec.DoubleValue nhpSize;
        public final ForgeConfigSpec.DoubleValue nhpValue;
        public final ForgeConfigSpec.BooleanValue nhpAnimation;
        public final ForgeConfigSpec.BooleanValue nhpFollowPlayers;
        public final ForgeConfigSpec.BooleanValue nhpWhiteList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> nhpmobList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> nhpBlockList;
        // Rare Health Pickup
        public final ForgeConfigSpec.IntValue rhpPickupDelay;
        public final ForgeConfigSpec.IntValue rhpChance;
        public final ForgeConfigSpec.DoubleValue rhpSize;
        public final ForgeConfigSpec.DoubleValue rhpValue;
        public final ForgeConfigSpec.BooleanValue rhpAnimation;
        public final ForgeConfigSpec.BooleanValue rhpFollowPlayers;
        public final ForgeConfigSpec.BooleanValue rhpWhiteList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> rhpmobList;



        private Config() {
            COMMON_BUILDER.push("Health Pickups - by the Decursio Team");
            COMMON_BUILDER.comment(
                    "I recommend changing the values from this file while the game isn't launched, you can still change some of these while the game is launched but it could cause issues!"
                    , "If you see a 'sanguinearsenal-common-<number>.toml.bak' file it means that something went wrong. " + "If you think that what you did was right and it was supposed to work, contact me."
            );

            healthPickupSound = COMMON_BUILDER
                    .comment("When this value is set to 'true' a sound will be played to the player that touches a health pickup entity.")
                    .define("healthPickupSound", true);
            COMMON_BUILDER.pop();
            COMMON_BUILDER.push("Normal Health Pickup");
            nhpPickupDelay = COMMON_BUILDER
                    .comment("This value represents the pickup delay of a Normal Health Pickup."
                            , "Set this value in ticks (20 ticks = 1 second) between 0 (no pickup delay) and 6000 (5 minutes pickup delay)")
                    .defineInRange("nhpPickupDelay", 20, 0, 6000);
            nhpChance = COMMON_BUILDER
                    .comment("This value represents the spawning chance of a Normal Health Pickup when a player kills a mob/aggressive entity."
                            , "Set this value between 0 (this will make it so it never spawns) and 100 (this will make it so it will always spawn)")
                    .defineInRange("nhpChance", 30, 0, 100);
            nhpValue = COMMON_BUILDER
                    .comment("This value represents the amount of health that the player receives when he touches a normal health pickup."
                            ,"Set this value between: 0.0 and 20.0")
                    .defineInRange("nhpValue", 3.0, 0, 20d);
            nhpSize = COMMON_BUILDER
                    .comment("This value represents the size of the Normal Health Pickup entity."
                            ,"Set this value between: 0.0 and 5.0")
                    .defineInRange("nhpSize", 0.3, 0, 5d);
            nhpAnimation = COMMON_BUILDER
                    .comment("Set this value to 'true' if you want to activate the fading animation of the Normal Health Pickup.")
                    .define("nhpAnimation", false);
            nhpFollowPlayers = COMMON_BUILDER
                    .comment("Set this value to 'true' if you want the Normal Health Pickup to follow players.")
                    .define("nhpFollowPlayers", false);
            nhpWhiteList = COMMON_BUILDER
                    .comment("A easy way of checking what value you should set here is by summoning a mob using the [/summon] command and saying what the entity name is." +
                            "\nSet this value to 'true' if you want the list below to be a Whitelist" +
                            "\nThis means that only the mob's names listed bellow will have a chance of spawning a Normal Health Pickup ")
                    .define("nhpWhitelist", false);
            nhpmobList = COMMON_BUILDER.comment("Normal Health Pickup Mob List")
                    .defineList("nhpMobBlacklist", Arrays.asList("Chicken"), entry -> true);
            nhpBlockList = COMMON_BUILDER.comment("Normal Health Pickup Block Whitelist")
                    .defineList("nhpBlockWhitelist", Arrays.asList("Spawner"), entry -> true);
            COMMON_BUILDER.pop();
            COMMON_BUILDER.push("Super Health Pickup");
            rhpPickupDelay = COMMON_BUILDER
                    .comment("This value represents the pickup delay of a Super Health Pickup."
                            , "Set this value in ticks (20 ticks = 1 second) between 0 (no pickup delay) and 6000 (5 minutes pickup delay)")
                    .defineInRange("rhpPickupDelay", 20, 0, 6000);
            rhpChance = COMMON_BUILDER
                    .comment("This value represents the spawning chance of a Super Health Pickup when a player kills a mob/aggressive entity.."
                            , "Set this value between 0 (this will make it so it never spawns) and 100 (this will make it so it will always spawn)")
                    .defineInRange("nhpChance", 15, 0, 100);
            rhpValue = COMMON_BUILDER
                    .comment("This value represents the amount of health that the player receives when he touches a Super Health Pickup."
                            ,"Set this value between: 0.0 and 20.0")
                    .defineInRange("rhpValue", 6.0, 0, 20d);
            rhpSize = COMMON_BUILDER
                    .comment("This value represents the size of the Super Health Pickup entity."
                            ,"Set this value between: 0.0 and 5.0")
                    .defineInRange("rhpSize", 0.3, 0, 5d);
            rhpAnimation = COMMON_BUILDER
                    .comment("Set this value to 'true' if you want to activate the fading animation of the Normal Health Pickup.")
                    .define("nhpAnimation", false);
            rhpFollowPlayers = COMMON_BUILDER
                    .comment("Set this value to 'true' if you want the Super Health Pickup to follow players.")
                    .define("nhpFollowPlayers", true);
            rhpWhiteList = COMMON_BUILDER
                    .comment("A easy way of checking what value you should set here is by summoning a mob using the [/summon] command and saying what the entity name is." +
                            "\nSet this value to 'true' if you want the list below to be a Whitelist" +
                            "\nThis means that only the mob's names listed bellow will have a chance of spawning a Super Health Pickup ")
                    .define("rhpWhitelist", true);
            rhpmobList = COMMON_BUILDER.comment("Super Health Pickup Mob List")
                    .defineList("rhpMobBlacklist", Arrays.asList("Wither", "Enderman", "Wither Skeleton", "Blaze"), entry -> true);
            COMMON_BUILDER.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_CONFIG = COMMON_BUILDER.build();
}
