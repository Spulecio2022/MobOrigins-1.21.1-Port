package me.ultrusmods.moborigins.entity.slime;

import me.ultrusmods.moborigins.entity.MobOriginsEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class OriginSlimeEntity extends Mob {
    private static final EntityDataAccessor<Integer> SLIME_SIZE;
    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UUID;
    private static final EntityDataAccessor<Float> RED;
    private static final EntityDataAccessor<Float> GREEN;
    private static final EntityDataAccessor<Float> BLUE;

    // TODO: Make it so you can make the slime stay in place

    public float targetStretch;
    public float stretch;
    public float lastStretch;
    private boolean onGroundLastTick;

    public OriginSlimeEntity(EntityType<? extends OriginSlimeEntity> type, Level level) {
        super(type, level);
        this.moveControl = new SlimeMoveControl(this);
    }

    // Goals

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimmingGoal(this));
        this.goalSelector.addGoal(2, new FaceTowardTargetGoal(this));
        this.goalSelector.addGoal(3, new RandomLookGoal(this));
        this.goalSelector.addGoal(5, new MoveGoal(this));
        this.goalSelector.addGoal(6, new OriginSlimeFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.targetSelector.addGoal(1, new OriginSlimeTrackOwnerAttackerGoal(this));
        this.targetSelector.addGoal(2, new OriginSlimeAttackWithOwnerGoal(this));
    }

    // Synched data

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLIME_SIZE, 1);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(RED, 0.6F);
        builder.define(GREEN, 1.0F);
        builder.define(BLUE, 0.5F);
    }

    // Size + attributes

    public void setSize(int size, boolean heal) {
        this.entityData.set(SLIME_SIZE, size);
        this.refreshDimensions();
        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance moveSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);

        if (maxHealth != null) {
            maxHealth.setBaseValue(size * size);
        }
        if (moveSpeed != null) {
            moveSpeed.setBaseValue(0.2F + 0.1F * (float) size);
        }
        if (attackDamage != null) {
            attackDamage.setBaseValue(size);
        }

        if (heal) {
            this.setHealth(this.getMaxHealth());
        }

        this.xpReward = 0;
    }

    public int getSize() {
        return this.entityData.get(SLIME_SIZE);
    }

    // Color

    public float getRed() {
        return this.entityData.get(RED);
    }

    public float getGreen() {
        return this.entityData.get(GREEN);
    }

    public float getBlue() {
        return this.entityData.get(BLUE);
    }

    public void setColor(float red, float green, float blue) {
        this.entityData.set(RED, red);
        this.entityData.set(GREEN, green);
        this.entityData.set(BLUE, blue);
    }

    // NBT

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Size", this.getSize() - 1);
        tag.putBoolean("wasOnGround", this.onGroundLastTick);
        UUID ownerId = this.getOwnerUuid();
        if (ownerId != null) {
            tag.putUUID("Owner", ownerId);
        }
        tag.putFloat("Red", this.getRed());
        tag.putFloat("Green", this.getGreen());
        tag.putFloat("Blue", this.getBlue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        int i = tag.getInt("Size");
        if (i < 0) {
            i = 0;
        }

        this.setSize(i + 1, false);
        super.readAdditionalSaveData(tag);
        this.onGroundLastTick = tag.getBoolean("wasOnGround");

        UUID ownerUuid = null;
        if (tag.hasUUID("Owner")) {
            ownerUuid = tag.getUUID("Owner");
        } else if (tag.contains("Owner")) {
            // Legacy string owner – Mojang removed ServerConfigHandler, so we skip resolving by name
            // TODO: If you really need name → UUID resolution, use your own lookup via GameProfile.
        }

        if (ownerUuid != null) {
            this.setOwnerUuid(ownerUuid);
        }

        if (tag.contains("Red") && tag.contains("Green") && tag.contains("Blue")) {
            this.setColor(tag.getFloat("Red"), tag.getFloat("Green"), tag.getFloat("Blue"));
        }
    }

    // Owner

    public void setOwnerUuid(UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(Player player) {
        this.setOwnerUuid(player.getUUID());
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast) return false;

        if (target instanceof TamableAnimal tame) {
            return tame.getOwner() != owner;
        } else if (target instanceof Player targetPlayer && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer)) {
            return false;
        } else if (target instanceof AbstractHorse horse) {
            return !horse.isTamed();
        }
        return true;
    }

    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUuid();
            if (uuid == null) return null;
            if (this.level() instanceof ServerLevel serverLevel) {
                return serverLevel.getPlayerByUUID(uuid);
            }
            return null;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public boolean canAttack(LivingEntity target) {
        return !this.isOwner(target) && super.canAttack(target);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public UUID getOwnerUuid() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    // Death message to owner

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
            LivingEntity owner = this.getOwner();
            if (owner instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(this.getCombatTracker().getDeathMessage());
            }
        }
        super.die(source);
    }

    // Misc properties

    public boolean isSmall() {
        return this.getSize() <= 1;
    }

    protected ParticleOptions getParticles() {
        return ParticleTypes.ITEM_SLIME;
    }

    protected boolean isDisallowedInPeaceful() {
        return this.getSize() > 0;
    }

    // Tick / stretch

    @Override
    public void tick() {
        this.stretch += (this.targetStretch - this.stretch) * 0.5F;
        this.lastStretch = this.stretch;
        super.tick();

        if (this.onGround() && !this.onGroundLastTick) {
            int size = this.getSize();

            for (int j = 0; j < size * 8; ++j) {
                float angle = this.random.nextFloat() * ((float) Math.PI * 2F);
                float g = this.random.nextFloat() * 0.5F + 0.5F;
                float dx = Mth.sin(angle) * (float) size * 0.5F * g;
                float dz = Mth.cos(angle) * (float) size * 0.5F * g;
                this.level().addParticle(this.getParticles(), this.getX() + dx, this.getY(), this.getZ() + dz, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(),
                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetStretch = -0.5F;
        } else if (!this.onGround() && this.onGroundLastTick) {
            this.targetStretch = 1.0F;
        }

        this.onGroundLastTick = this.onGround();
        this.updateStretch();
    }

    protected void updateStretch() {
        this.targetStretch *= 0.6F;
    }

    protected int getTicksUntilNextJump() {
        return this.random.nextInt(20) + 10;
    }

    // Dimensions

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SLIME_SIZE.equals(key)) {
            this.refreshDimensions();
            this.setYRot(this.getYHeadRot());
            this.yBodyRot = this.getYHeadRot();
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.onSyncedDataUpdated(key);
    }

    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.625F * dimensions.height();
    }

    @Override
    public float getScale() {
        return 0.255F * this.getSize();
    }

    // Removal / splitting

    @Override
    public void remove(RemovalReason reason) {
        int size = this.getSize();
        if (!this.level().isClientSide && size > 1 && this.isDeadOrDying()) {
            var name = this.getCustomName();
            boolean noAI = this.isNoAi();
            float f = size / 4.0F;
            int j = size / 2;
            int count = 2 + this.random.nextInt(3);

            for (int l = 0; l < count; ++l) {
                float g = ((l % 2) - 0.5F) * f;
                float h = ((l / 2) - 0.5F) * f;
                var slime = new OriginSlimeEntity(MobOriginsEntities.ORIGIN_SLIME, this.level());

                slime.setCustomName(name);
                slime.setNoAi(noAI);
                slime.setOwnerUuid(this.getOwnerUuid());
                slime.setInvulnerable(this.isInvulnerable());
                slime.setSize(j, true);
                slime.setColor(this.getRed(), this.getGreen(), this.getBlue());
                slime.moveTo(this.getX() + g, this.getY() + 0.5D, this.getZ() + h,
                        this.random.nextFloat() * 360.0F, 0.0F);
                this.level().addFreshEntity(slime);
            }
        }

        super.remove(reason);
    }

    // Spawning

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                        DifficultyInstance difficulty,
                                        MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {
        int i = this.random.nextInt(3);
        if (i < 2 && this.random.nextFloat() < 0.5F * difficulty.getEffectiveDifficulty()) {
            ++i;
        }

        int size = 1 << i;
        this.setSize(size, true);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    // Collision / attack

    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (entity instanceof LivingEntity living && this.isOwner(living)) {
            return;
        }

        LivingEntity owner = this.getOwner();
        if (entity instanceof LivingEntity target
                && this.canAttackWithOwner(target, owner)
                && this.canAttack()
                && !(entity instanceof OriginSlimeEntity)) {
            this.doDamage(target);
        }
    }

    protected void doDamage(LivingEntity target) {
        if (this.isAlive()) {
            int size = this.getSize();
            double maxDistSq = 0.6D * (double) size * 0.6D * (double) size;
            if (this.distanceToSqr(target) < maxDistSq
                    && this.hasLineOfSight(target)
                    && target.hurt(this.damageSources().mobAttack(this), this.getDamageAmount())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F,
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doHurtTarget(target);
            }
        }
    }

    protected boolean canAttack() {
        return true;
    }

    protected float getDamageAmount() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    // Sounds

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isSmall() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmall() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isSmall() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F * (float) this.getSize();
    }

    protected boolean makesJumpSound() {
        return this.getSize() > 0;
    }

    private float getJumpSoundPitch() {
        float f = this.isSmall() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    protected SoundEvent getJumpSound() {
        return this.isSmall() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    // Jump / movement

    @Override
    public void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    // Static data keys

    static {
        SLIME_SIZE = SynchedEntityData.defineId(OriginSlimeEntity.class, EntityDataSerializers.INT);
        OWNER_UUID = SynchedEntityData.defineId(OriginSlimeEntity.class, EntityDataSerializers.OPTIONAL_UUID);
        RED = SynchedEntityData.defineId(OriginSlimeEntity.class, EntityDataSerializers.FLOAT);
        GREEN = SynchedEntityData.defineId(OriginSlimeEntity.class, EntityDataSerializers.FLOAT);
        BLUE = SynchedEntityData.defineId(OriginSlimeEntity.class, EntityDataSerializers.FLOAT);
    }

    // Inner classes – goals & move control

    static class MoveGoal extends Goal {
        private final OriginSlimeEntity slime;

        public MoveGoal(OriginSlimeEntity slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        @Override
        public void tick() {
            ((SlimeMoveControl) this.slime.getMoveControl()).move(1.0D);
        }
    }

    static class SwimmingGoal extends Goal {
        private final OriginSlimeEntity slime;

        public SwimmingGoal(OriginSlimeEntity slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            this.slime.getNavigation().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava())
                    && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        @Override
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }
            ((SlimeMoveControl) this.slime.getMoveControl()).move(1.2D);
        }
    }

    static class RandomLookGoal extends Goal {
        private final OriginSlimeEntity slime;
        private float targetYaw;
        private int timer;

        public RandomLookGoal(OriginSlimeEntity slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.getTarget() == null
                    && (this.slime.onGround()
                    || this.slime.isInWater()
                    || this.slime.isInLava()
                    || this.slime.hasEffect(MobEffects.LEVITATION))
                    && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        @Override
        public void tick() {
            if (--this.timer <= 0) {
                this.timer = 40 + this.slime.getRandom().nextInt(60);
                this.targetYaw = this.slime.getRandom().nextInt(360);
            }
            ((SlimeMoveControl) this.slime.getMoveControl()).look(this.targetYaw, false);
        }
    }

    static class FaceTowardTargetGoal extends Goal {
        private final OriginSlimeEntity slime;
        private int ticksLeft;

        public FaceTowardTargetGoal(OriginSlimeEntity slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.slime.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (target instanceof Player player && player.getAbilities().invulnerable) return false;
            return this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        @Override
        public void start() {
            this.ticksLeft = 300;
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.slime.getTarget();
            if (target == null || !target.isAlive()) return false;
            if (target instanceof Player player && player.getAbilities().invulnerable) return false;
            return --this.ticksLeft > 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.slime.getTarget();
            if (target == null) return;
            this.slime.lookAt(target, 10.0F, 10.0F);
            ((SlimeMoveControl) this.slime.getMoveControl())
                    .look(this.slime.getYRot(), this.slime.canAttack());
        }
    }

    static class SlimeMoveControl extends MoveControl {
        private float targetYaw;
        private int ticksUntilJump;
        private final OriginSlimeEntity slime;
        private boolean jumpOften;

        public SlimeMoveControl(OriginSlimeEntity slime) {
            super(slime);
            this.slime = slime;
            this.targetYaw = slime.getYRot();
        }

        public void look(float targetYaw, boolean jumpOften) {
            this.targetYaw = targetYaw;
            this.jumpOften = jumpOften;
        }

        public void move(double speed) {
            this.speedModifier = speed;
            this.operation = Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.targetYaw, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();

            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed(
                            (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.ticksUntilJump-- <= 0) {
                        this.ticksUntilJump = this.slime.getTicksUntilNextJump();
                        if (this.jumpOften) {
                            this.ticksUntilJump /= 3;
                        }

                        this.slime.getJumpControl().jump();
                        if (this.slime.makesJumpSound()) {
                            this.slime.playSound(
                                    this.slime.getJumpSound(),
                                    this.slime.getSoundVolume(),
                                    this.slime.getJumpSoundPitch()
                            );
                        }
                    } else {
                        this.slime.setZza(0.0F);
                        this.slime.setXxa(0.0F);
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed(
                            (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }
}