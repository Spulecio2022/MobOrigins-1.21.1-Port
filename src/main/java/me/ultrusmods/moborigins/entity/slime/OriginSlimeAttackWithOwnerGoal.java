package me.ultrusmods.moborigins.entity.slime;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OriginSlimeAttackWithOwnerGoal extends TargetGoal {
    private final OriginSlimeEntity originSlimeEntity;
    private LivingEntity attacking;
    private int lastAttackTime;

    public OriginSlimeAttackWithOwnerGoal(OriginSlimeEntity originSlimeEntity) {
        super(originSlimeEntity, false);
        this.originSlimeEntity = originSlimeEntity;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.originSlimeEntity.getOwner();
        if (owner == null) {
            return false;
        }

        this.attacking = owner.getLastHurtMob();
        int timestamp = owner.getLastHurtMobTimestamp();

        return this.attacking != null
                && timestamp != this.lastAttackTime
                && this.canAttack(this.attacking, TargetingConditions.DEFAULT)
                && this.originSlimeEntity.canAttackWithOwner(this.attacking, owner);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);

        LivingEntity owner = this.originSlimeEntity.getOwner();
        if (owner != null) {
            this.lastAttackTime = owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
