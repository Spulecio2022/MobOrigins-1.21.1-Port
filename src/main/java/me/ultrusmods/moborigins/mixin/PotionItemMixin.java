package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    @Inject(
            method = "finishUsingItem",
            at = @At("RETURN")
    )
    private void moborigins$betterPotions(ItemStack stack, Level level, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {

        if (!MobOriginsPowers.hasPower(user, MobOriginsPowers.BETTER_POTIONS)) {
            return;
        }

        // 1.21.1: Potion effects are stored in a DataComponent
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return;
        }

        for (MobEffectInstance effect : contents.getAllEffects()) {

            MobEffectInstance boosted = new MobEffectInstance(
                    effect.getEffect(),
                    effect.getDuration() * 2,
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            );

            user.addEffect(boosted);
        }
    }
}