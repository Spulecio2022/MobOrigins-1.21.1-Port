package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.FallSoundPower;
import me.ultrusmods.moborigins.power.ModifyAttackDistanceScalingFactorPower;
import me.ultrusmods.moborigins.power.TotemChancePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    // Totem break chance
    @Inject(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            )
    )
    private void moborigins$modifyTotemBreakChance(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        var component = PowerHolderComponent.getNullable(self);
        if (component == null) return;

        var powers = component.getPowers(true).stream()
                .filter(p -> p.getId().equals(MobOriginsPowers.TOTEM_CHANCE))
                .map(p -> (TotemChancePower) component.getPowerType(p))
                .toList();

        if (!powers.isEmpty()) {
            float chance = powers.stream()
                    .map(TotemChancePower::getBreakChance)
                    .reduce(0f, Float::sum);

            if (self.getRandom().nextFloat() < chance) {
                // Undo the shrink
                ItemStack stack = self.getItemInHand(self.getUsedItemHand());
                if (!stack.isEmpty()) {
                    stack.grow(1);
                }
            }
        }
    }

    // Reach attribute modification
    @Inject(
            method = "getAttributeValue",
            at = @At("RETURN"),
            cancellable = true
    )
    private void moborigins$modifyReachAttribute(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute.is(Attributes.ENTITY_INTERACTION_RANGE)) {
            LivingEntity self = (LivingEntity)(Object)this;

            double base = cir.getReturnValue();

            float modified = PowerHolderComponent.modify(
                    self,
                    ModifyAttackDistanceScalingFactorPower.class,
                    (float) base,
                    p -> true
            );

            cir.setReturnValue((double) modified);
        }
    }

    // Small + big fall sounds
    @Inject(
            method = "getFallSounds",
            at = @At("RETURN"),
            cancellable = true
    )
    private void moborigins$overrideFallSounds(CallbackInfoReturnable<LivingEntity.Fallsounds> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        var component = PowerHolderComponent.getNullable(self);
        if (component == null) return;

        var powers = component.getPowers(true).stream()
                .filter(p -> p.getId().equals(MobOriginsPowers.FALL_SOUNDS))
                .map(p -> (FallSoundPower) component.getPowerType(p))
                .toList();

        if (!powers.isEmpty()) {
            var small = powers.get(0).getSmallSound();
            var big = powers.get(0).getBigSound();
            cir.setReturnValue(new LivingEntity.Fallsounds(small, big));
        }
    }
}