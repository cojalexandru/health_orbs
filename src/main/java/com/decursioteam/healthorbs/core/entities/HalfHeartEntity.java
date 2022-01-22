package com.decursioteam.healthorbs.core.entities;

import com.decursioteam.healthorbs.HealthOrbs;
import com.decursioteam.healthorbs.core.HealthPickupsConfig;
import com.decursioteam.healthorbs.core.Registry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class HalfHeartEntity extends Entity {
    public int tickCount;
    public int age;
    public int throwTime;
    private int health = 5;
    public int value;
    private PlayerEntity followingPlayer;
    private int followingTime;

    public HalfHeartEntity(World world, double x, double y, double z) {
        this(HealthOrbs.HALF_HEART_ENTITY.get(), world);
        this.setPos(x, y, z);
        this.yRotO = (float) (this.random.nextDouble() * 360.0D);
        this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
    }

    public HalfHeartEntity(EntityType<? extends HalfHeartEntity> p_i50382_1_, World p_i50382_2_) {
        super(p_i50382_1_, p_i50382_2_);
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
            BlockPos pos = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
            f = this.level.getBlockState(pos).getSlipperiness(this.level, pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double) f, 0.98D, (double) f));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
        }
        if(HealthPickupsConfig.COMMON.nhpFollowPlayers.get()) {
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
                Vector3d vector3d = new Vector3d(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double) this.followingPlayer.getEyeHeight() / 2.0D - this.getY(), this.followingPlayer.getZ() - this.getZ());
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
            this.remove();
        }

    }

    private void setUnderwaterMovement() {
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x * (double) 0.99F, Math.min(vector3d.y + (double) 5.0E-4F, (double) 0.06F), vector3d.z * (double) 0.99F);
    }

    protected void doWaterSplashEffect() {
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.level.isClientSide || this.removed) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(p_70097_1_)) {
            return false;
        } else {
            this.markHurt();
            this.health = (int) ((float) this.health - p_70097_2_);
            if (this.health <= 0) {
                this.remove();
            }

            return false;
        }
    }

    @Override
    public void playerTouch(PlayerEntity playerEntity) {
        if (!this.level.isClientSide) {
            if (this.throwTime == 0) {
                if (this.age >= HealthPickupsConfig.COMMON.nhpPickupDelay.get()) {
                    playerEntity.take(this, 1);
                    playerEntity.heal(HealthPickupsConfig.COMMON.nhpValue.get().floatValue());
                    if(HealthPickupsConfig.COMMON.healthPickupSound.get())
                        this.playSound(Registry.GET_HEART_SOUND, 0.4F, 0.95F);
                    this.remove();
                }
            }
        }
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        p_213281_1_.putShort("Health", (short) this.health);
        p_213281_1_.putShort("Age", (short) this.age);
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        this.health = p_70037_1_.getShort("Health");
        this.age = p_70037_1_.getShort("Age");
    }

    public boolean isAttackable() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
