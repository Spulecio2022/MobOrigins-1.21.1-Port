package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.AddExperienceToResourcePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbEntityMixin extends Entity {

    public ExperienceOrbEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "playerTouch",
            at = @At("TAIL")
    )
    private void moborigins$addXpToResource(Player player, CallbackInfo ci) {

        PowerHolderComponent component = PowerHolderComponent.getNullable(player);
        if (component == null) return;

        List<AddExperienceToResourcePower> powers = component.getPowers(true).stream()
                .filter(power -> power.getId().equals(MobOriginsPowers.ADD_EXPERIENCE_TO_RESOURCE))
                .map(power -> (AddExperienceToResourcePower) component.getPowerType(power))
                .toList();

        for (AddExperienceToResourcePower power : powers) {
            power.addToResource(0);
        }
    }
}
