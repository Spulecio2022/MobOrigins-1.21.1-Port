package me.ultrusmods.moborigins.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.HostileAxolotlsPower;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.AxolotlAttackablesSensor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxolotlAttackablesSensor.class)
public class AxolotlAttackablesSensorMixin {

    @ModifyReturnValue(method = "isHostileTarget", at = @At("RETURN"))
    private boolean moborigins$makeHostile(boolean original, LivingEntity target) {

        if (original) {
            return true;
        }

        var component = PowerHolderComponent.getNullable(target);
        if (component == null) {
            return false;
        }

        boolean hasPower = component.getPowers(true).stream()
                .map(component::getPowerType)
                .anyMatch(pt -> pt instanceof HostileAxolotlsPower);

        return hasPower;
    }
}