package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.registry.ApoliRegistries;
import me.ultrusmods.moborigins.MobOriginsMod;
import me.ultrusmods.moborigins.power.DyeableModelColorPower;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;

public class SetDyeableModelColorActionType extends EntityActionType {

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("set_dyeable_model_color"),
                    SetDyeableModelColorActionType::new
            );

    @Override
    public void accept(EntityActionContext context) {

        var entity = context.entity();
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        var component = PowerHolderComponent.getNullable(entity);
        if (component == null) return;

        component.getPowerTypes(DyeableModelColorPower.class)
                .forEach(p -> p.applyColor(living));
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}