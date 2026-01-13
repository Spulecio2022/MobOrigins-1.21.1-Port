package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @Inject(
            method = "canEntityWalkOnPowderSnow",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void moborigins$canWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {

        if (MobOriginsPowers.hasPower(entity, MobOriginsPowers.WALK_ON_POWDER_SNOW)) {
            cir.setReturnValue(true);
        }
    }
}
