package com.decursioteam.pickableorbs.entities;

import com.decursioteam.pickableorbs.PickableOrbs;
import com.decursioteam.pickableorbs.datagen.Orbs;
import com.decursioteam.pickableorbs.datagen.OrbsData;
import com.decursioteam.pickableorbs.registries.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class HalfHeartEntity extends Entity {
    public int tickCount;
    public int age;
    public int throwTime;
    private int health = 5;
    protected final Orbs orbData;
    protected final String orbType;
    private Player followingPlayer;
    private int followingTime;

    public HalfHeartEntity(EntityType<HalfHeartEntity> type, Level world, double x, double y, double z, String orbType, Orbs orbData) {
        super(type, world);
        this.orbData = orbData;
        this.orbType = orbType;
        this.setPos(x, y, z);
        this.yRotO = (float) (this.random.nextDouble() * 360.0D);
        this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
    }

    public HalfHeartEntity(EntityType<HalfHeartEntity> type, Level world, String orbType) {
        super(type, world);
        this.orbType = orbType;
        this.orbData = OrbsData.getOrbData(orbType);
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    protected void defineSynchedData() {
    }

    public void tick() {
        super.tick();
        if (this.throwTime > 0) {
            --this.throwTime;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }

        if (this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), (double) 0.2F, (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
            this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        if (!this.level.noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround) {
            BlockPos pos = new BlockPos((int) this.getX(), (int) (this.getY() - 1.0D), (int) this.getZ());
            f = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double) f, 0.98D, (double) f));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
        }

        if(orbData.getExtraData().getFollowPlayer()) {
            if (this.followingTime < this.tickCount - 20 + this.getId() % 100) {
                if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0D) {
                    this.followingPlayer = this.level.getNearestPlayer(this, 8.0D);
                }

                this.followingTime = this.tickCount;
            }

            if (this.followingPlayer != null && this.followingPlayer.isSpectator()) {
                this.followingPlayer = null;
            }

            if (this.followingPlayer != null) {
                Vec3 vector3d = new Vec3(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double) this.followingPlayer.getEyeHeight() / 2.0D - this.getY(), this.followingPlayer.getZ() - this.getZ());
                double d1 = vector3d.lengthSqr();
                if (d1 < 64.0D) {
                    double d2 = 1.0D - Math.sqrt(d1) / 8.0D;
                    this.setDeltaMovement(this.getDeltaMovement().add(vector3d.normalize().scale(d2 * d2 * 0.1D)));
                }
            }
        }

        ++this.tickCount;
        ++this.age;
        if (this.age >= 6000) {
            this.remove(RemovalReason.DISCARDED);
        }

    }

    private void setUnderwaterMovement() {
        Vec3 vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x * (double) 0.99F, Math.min(vector3d.y + (double) 5.0E-4F, (double) 0.06F), vector3d.z * (double) 0.99F);
    }

    protected void doWaterSplashEffect() {
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.level.isClientSide || this.isRemoved()) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(p_70097_1_)) {
            return false;
        } else {
            this.markHurt();
            this.health = (int) ((float) this.health - p_70097_2_);
            if (this.health <= 0) {
                this.remove(RemovalReason.KILLED);
            }

            return false;
        }
    }

    @Override
    public void playerTouch(Player playerEntity) {
        if (!this.level.isClientSide) {
            if (this.throwTime == 0) {
                if (this.age >= orbData.getExtraData().getPickupDelay()) {
                    playerEntity.take(this, 1);
                    int effectMultiplier = orbData.getData().getEffectMultiplier();
                    int effectDuration = orbData.getData().getEffectDuration();
                    if(Objects.equals(orbData.getData().getType(), new ResourceLocation("pickableorbs:percentage_healing"))){
                        playerEntity.heal((float) (playerEntity.getMaxHealth() * ((float)orbData.getData().getEffectMultiplier()) / 100.0));
                    }
                    else try {
                        playerEntity.addEffect(new MobEffectInstance(Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getValue(orbData.getData().getType())), effectDuration, effectMultiplier));
                    } catch (Exception e) {
                        PickableOrbs.LOGGER.error("[PickableOrbs] - The orb type of: " + orbData.getData().getName() + " is invalid.");
                    }


                    if(!orbData.getExtraData().getPickupMessage().isEmpty())
                        playerEntity.displayClientMessage(Component.literal(orbData.getExtraData().getPickupMessage()), true);
                    if(orbData.getExtraData().getSound()) this.playSound(Registry.GET_HEART_SOUND.get(), 0.4F, 0.95F);
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        p_213281_1_.putShort("Health", (short) this.health);
        p_213281_1_.putShort("Age", (short) this.age);
    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        this.health = p_70037_1_.getShort("Health");
        this.age = p_70037_1_.getShort("Age");
    }

    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }
}
