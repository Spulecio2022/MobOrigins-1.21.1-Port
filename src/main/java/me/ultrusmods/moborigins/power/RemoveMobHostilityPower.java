package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class RemoveMobHostilityPower extends PowerType {

    private final EntityCondition entityCondition;

    public RemoveMobHostilityPower(EntityCondition entityCondition) {
        super();
        this.entityCondition = entityCondition;
    }

    /** Checks whether this mob should ignore hostility toward the holder */
    public boolean apply(Entity mob, LivingEntity holder) {
        return mob instanceof LivingEntity &&
                (entityCondition == null || entityCondition.test(mob));
    }

    // -------------------------
    // DATA FACTORY
    // -------------------------
    public static final TypedDataObjectFactory<RemoveMobHostilityPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("entity_condition", EntityCondition.DATA_TYPE.optional(), null),
                    data -> {
                        EntityCondition cond = data.get("entity_condition");
                        return new RemoveMobHostilityPower(cond);
                    },
                    (power, serializableData) -> serializableData.instance()
            );

    // -------------------------
    // CONFIG
    // -------------------------
    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("remove_mob_hostility"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}