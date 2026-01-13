package me.ultrusmods.moborigins.entity.slime;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OriginSlimeTrackOwnerAttackerGoal extends TargetGoal {
    private final OriginSlimeEntity tameable;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public OriginSlimeTrackOwnerAttackerGoal(OriginSlimeEntity tameable) {
        super(tameable, false);
        this.tameable = tameable;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.tameable.getOwner();
        if (owner == null) {
            return false;
        }

        this.attacker = owner.getLastHurtByMob();
        int time = owner.getLastHurtByMobTimestamp();

        return this.attacker != null
                && time != this.lastAttackedTime
                && this.canAttack(this.attacker, TargetingConditions.DEFAULT)
                && this.tameable.canAttackWithOwner(this.attacker, owner);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);

        LivingEntity owner = this.tameable.getOwner();
        if (owner != null) {
            this.lastAttackedTime = owner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}