package com.decursioteam.pickableorbs.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ExtraOptions {

    public static final ExtraOptions DEFAULT = new ExtraOptions("", false, true, false, 20);


    public static final Codec<ExtraOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("pickup-message").orElse("").forGetter(ExtraOptions::getPickupMessage),
            Codec.BOOL.fieldOf("animation").orElse(false).forGetter(ExtraOptions::getAnimation),
            Codec.BOOL.fieldOf("sound").orElse(false).forGetter(ExtraOptions::getSound),
            Codec.BOOL.fieldOf("follow-player").orElse(false).forGetter(ExtraOptions::getFollowPlayer),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("pickup-delay").orElse(20).forGetter(ExtraOptions::getPickupDelay)
    ).apply(instance, ExtraOptions::new));

    protected boolean sound;
    protected String pickupMessage;
    protected boolean animation;
    protected boolean followPlayer;
    protected int pickupDelay;

    private ExtraOptions(String pickupMessage, boolean animation, boolean sound, boolean followPlayer, int pickupDelay){
        this.sound = sound;
        this.pickupMessage = pickupMessage;
        this.animation = animation;
        this.followPlayer = followPlayer;
        this.pickupDelay = pickupDelay;
    }

    public String getPickupMessage() {
        return pickupMessage;
    }

    public boolean getAnimation() {
        return animation;
    }

    public boolean getSound() {
        return sound;
    }

    public boolean getFollowPlayer() {
        return followPlayer;
    }

    public int getPickupDelay() {
        return pickupDelay;
    }


    public ExtraOptions toImmutable() {
        return this;
    }

    public static class Mutable extends ExtraOptions {

        public Mutable(String pickupMessage, boolean animation, boolean sound, boolean followPlayer, int pickupDelay) {
            super(pickupMessage, animation, sound, followPlayer, pickupDelay);
        }

        public Mutable setAnimation(boolean animation) {
            this.animation = animation;
            return this;
        }

        public Mutable setSound(boolean sound) {
            this.sound = sound;
            return this;
        }

        public Mutable setFollowPlayer(boolean followPlayer) {
            this.followPlayer = followPlayer;
            return this;
        }

        @Override
        public ExtraOptions toImmutable() {
            return new ExtraOptions(this.pickupMessage, this.animation, this.sound, this.followPlayer, this.pickupDelay);
        }
    }
}
