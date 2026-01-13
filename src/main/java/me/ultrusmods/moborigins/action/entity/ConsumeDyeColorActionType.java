package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.MobOriginsMod;
import me.ultrusmods.moborigins.power.DyeableModelColorPower;
import io.github.apace100.apoli.registry.ApoliRegistries;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

public class ConsumeDyeColorActionType extends EntityActionType {

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("consume_dye"),
                    ConsumeDyeColorActionType::new
            );

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();

        if (entity instanceof Player player) {

            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof DyeItem dyeItem) {

                int rgb = dyeItem.getDyeColor().getTextureDiffuseColor();

                float r = ((rgb >> 16) & 0xFF) / 255f;
                float g = ((rgb >> 8) & 0xFF) / 255f;
                float b = (rgb & 0xFF) / 255f;

                float[] col = new float[] { r, g, b };

                // Correct Apoli 2.12+ pattern
                PowerHolderComponent.getPowerTypes(entity, DyeableModelColorPower.class)
                        .forEach(power -> power.blendColor(col));

                stack.shrink(1);
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