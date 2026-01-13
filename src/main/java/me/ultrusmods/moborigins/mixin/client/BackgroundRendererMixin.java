package me.ultrusmods.moborigins.mixin.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.FogPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class BackgroundRendererMixin {

    @ModifyVariable(
            method = "renderSky",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"
            ),
            ordinal = 0
    )
    private Vec3 moborigins$modifySkyColor(Vec3 original) {

        if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {

            PowerHolderComponent component = PowerHolderComponent.getNullable(living);
            if (component == null) return original;

            List<FogPower> fogPowers = component.getPowerTypes().stream()
                    .filter(p -> p instanceof FogPower)
                    .map(p -> (FogPower) p)
                    .toList();

            if (!fogPowers.isEmpty()) {
                float red   = fogPowers.stream().map(FogPower::getRed).reduce((a, b2) -> a * b2).orElse(1f);
                float green = fogPowers.stream().map(FogPower::getGreen).reduce((a, b2) -> a * b2).orElse(1f);
                float blue  = fogPowers.stream().map(FogPower::getBlue).reduce((a, b2) -> a * b2).orElse(1f);

                return new Vec3(red, green, blue);
            }
        }

        return original;
    }
}