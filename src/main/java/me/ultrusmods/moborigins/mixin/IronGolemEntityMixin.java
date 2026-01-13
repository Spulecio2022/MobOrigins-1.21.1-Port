package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IronGolem.class, priority = 1000)
public class IronGolemEntityMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void moborigins$addPillagerAlignedTargeting(CallbackInfo ci) {
        IronGolem self = (IronGolem)(Object)this;

        GoalSelector selector =
                ((MobTargetSelectorAccessor) self).moborigins$getTargetSelector();

        selector.addGoal(
                3,
                new NearestAttackableTargetGoal<>(
                        self,
                        LivingEntity.class,
                        5,
                        true,
                        false,
                        entity -> MobOriginsPowers.hasPower(entity, MobOriginsPowers.PILLAGER_ALIGNED)
                )
        );
    }
}