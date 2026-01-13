package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.MimicEnchantPower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(
            method = "getEnchantmentLevel",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void moborigins$mimicEnchant(
            Holder<Enchantment> enchantment,
            LivingEntity entity,
            CallbackInfoReturnable<Integer> cir
    ) {

        PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
        if (component == null) return;

        List<MimicEnchantPower> powers = component.getPowers(true).stream()
                .filter(power -> power.getId().equals(MobOriginsPowers.ENCHANTMENT))
                .map(power -> (MimicEnchantPower) component.getPowerType(power))
                .toList();

        for (MimicEnchantPower power : powers) {

            // Your power returns a raw Enchantment
            Enchantment mimic = power.getEnchantment();

            // Wrap it in a Holder so types match
            Holder<Enchantment> mimicHolder = Holder.direct(mimic);

            // Compare Holder<Enchantment> correctly
            if (mimicHolder.value() == enchantment.value()) {
                cir.setReturnValue(power.getLevel());
                return;
            }
        }
    }
}