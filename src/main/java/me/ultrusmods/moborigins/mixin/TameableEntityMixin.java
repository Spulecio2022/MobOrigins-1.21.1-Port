package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.ActionOnEntityTamePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TamableAnimal.class)
public abstract class TameableEntityMixin extends Animal {

    protected TameableEntityMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tame", at = @At("TAIL"))
    private void moborigins$onTamed(Player player, CallbackInfo ci) {

        PowerHolderComponent component = PowerHolderComponent.getNullable(player);
        if (component == null) return;

        List<ActionOnEntityTamePower> powers = component.getPowers(true).stream()
                .filter(power -> power.getId().equals(MobOriginsPowers.ACTION_ON_ENTITY_TAME))
                .map(power -> (ActionOnEntityTamePower) component.getPowerType(power))
                .filter(p -> p.shouldExecute(this))
                .toList();

        powers.forEach(p -> p.executeAction(this));
    }
}