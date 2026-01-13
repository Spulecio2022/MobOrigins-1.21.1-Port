package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

import me.ultrusmods.moborigins.MobOriginsMod;
import me.ultrusmods.moborigins.entity.MobOriginsEntities;
import me.ultrusmods.moborigins.entity.slime.OriginSlimeEntity;
import me.ultrusmods.moborigins.power.DyeableModelColorPower;

import io.github.apace100.apoli.component.PowerHolderComponent;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SummonSlimeActionType extends EntityActionType {

    private final int size;

    public SummonSlimeActionType(int size) {
        this.size = size;
    }

    @Override
    public void accept(EntityActionContext context) {
        Entity entity = context.entity();

        OriginSlimeEntity slime = new OriginSlimeEntity(
                MobOriginsEntities.ORIGIN_SLIME,
                entity.level()
        );

        slime.moveTo(
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                entity.getYRot(),
                entity.getXRot()
        );

        if (entity instanceof Player player) {
            slime.setOwner(player);
        }

        slime.setSize(size, true);
        slime.setCustomName(entity.getDisplayName());

        PowerHolderComponent.getOptional(entity).ifPresent(pc -> {
            var powers = pc.getPowerTypes(DyeableModelColorPower.class);

            if (!powers.isEmpty()) {
                var power = powers.get(0);

                slime.setColor(
                        power.getRed(),
                        power.getGreen(),
                        power.getBlue()
                );
            }
        });
        entity.level().addFreshEntity(slime);
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    // -----------------------------
    // DATA + CONFIG
    // -----------------------------

    public static final TypedDataObjectFactory<EntityActionType> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("size", SerializableDataTypes.INT, 2),
                    data -> new SummonSlimeActionType(
                            data.getInt("size")
                    ),
                    (action, data) -> data.instance()
            );

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.of(
                    MobOriginsMod.id("summon_slime"),
                    DATA_FACTORY
            );

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}
