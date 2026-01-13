package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.ActionOnBreedAnimalPower;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Animal.class)
public abstract class AnimalEntityMixin {

    @Shadow @Nullable
    public abstract ServerPlayer getLoveCause();

    @Inject(
            method = "finalizeSpawnChildFromBreeding",
            at = @At("TAIL")
    )
    private void moborigins$onBreed(ServerLevel world, Animal otherParent, @Nullable AgeableMob child, CallbackInfo ci) {

        if (child == null) {
            return;
        }

        Optional.ofNullable(this.getLoveCause())
                .or(() -> Optional.ofNullable(otherParent.getLoveCause()))
                .ifPresent(player -> {
                    PowerHolderComponent component = PowerHolderComponent.getNullable(player);
                    if (component == null) return;

                    component.getPowers(true).stream()
                            .map(component::getPowerType)
                            .filter(pt -> pt instanceof ActionOnBreedAnimalPower)
                            .map(pt -> (ActionOnBreedAnimalPower) pt)
                            .filter(p -> p.shouldExecute(child))
                            .forEach(p -> p.executeAction(child));
                });
    }
}