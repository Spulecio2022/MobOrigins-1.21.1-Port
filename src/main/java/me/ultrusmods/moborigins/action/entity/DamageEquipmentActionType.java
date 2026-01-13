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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class DamageEquipmentActionType extends EntityActionType {

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("damage_equipment"),
                    DamageEquipmentActionType::new
            );

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();

        if (entity instanceof LivingEntity living) {

            ItemStack stack = living.getItemBySlot(EquipmentSlot.MAINHAND);

            if (!stack.isEmpty() && stack.isDamageableItem()) {
                stack.hurtAndBreak(1, living, null);
            }
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