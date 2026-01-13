package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;

import io.github.apace100.apoli.registry.ApoliRegistries;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class JumpActionType extends EntityActionType {

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("jump"),
                    JumpActionType::new
            );

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();

        if (entity instanceof LivingEntity living) {
            living.jumpFromGround();
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}