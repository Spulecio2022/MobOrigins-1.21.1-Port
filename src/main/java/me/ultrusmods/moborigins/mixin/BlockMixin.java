package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.BouncePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(
            method = "fallOn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void moborigins$bounceOnLand(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {

        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        if (living.isSuppressingBounce()) {
            return;
        }

        PowerHolderComponent component = PowerHolderComponent.getNullable(living);
        if (component == null) return;

        // Collect all BouncePower instances
        List<BouncePower> powers = component.getPowers(true).stream()
                .filter(power -> power.getId().equals(MobOriginsPowers.BOUNCE))
                .map(power -> (BouncePower) component.getPowerType(power))
                .toList();

        if (powers.isEmpty()) return;

        double multiplier = powers.stream()
                .mapToDouble(BouncePower::getMultiplier)
                .sum();

        living.setDeltaMovement(living.getDeltaMovement().multiply(1.0, multiplier, 1.0));
        ci.cancel();
    }
}