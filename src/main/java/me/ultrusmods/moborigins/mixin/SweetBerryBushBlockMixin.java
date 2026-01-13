package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void moborigins$carefulGatherer(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {

        if (MobOriginsPowers.hasPower(entity, MobOriginsPowers.CAREFUL_GATHERER)) {
            ci.cancel();
        }
    }
}