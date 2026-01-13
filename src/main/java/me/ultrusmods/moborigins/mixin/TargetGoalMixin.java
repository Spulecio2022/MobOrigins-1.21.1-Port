package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.RemoveMobHostilityPower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin extends Goal {

    @Shadow protected Mob mob;

    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private void moborigins$preventHostility(CallbackInfo ci) {

        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        PowerHolderComponent component = PowerHolderComponent.getNullable(target);
        if (component == null) return;

        boolean shouldCancel = component.getPowers(true).stream()
                .filter(p -> p.getId().equals(MobOriginsPowers.REMOVE_MOB_HOSTILITY))
                .map(p -> (RemoveMobHostilityPower) component.getPowerType(p))
                .anyMatch(p -> p.apply(this.mob, target));

        if (shouldCancel) {
            this.stop();
            ci.cancel();
        }
    }
}