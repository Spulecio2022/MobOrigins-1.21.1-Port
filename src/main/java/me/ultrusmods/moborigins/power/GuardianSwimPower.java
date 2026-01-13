package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GuardianSwimPower extends PowerType {

    public static final TypedDataObjectFactory<GuardianSwimPower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData(), // no fields needed
                    (data, condition) -> new GuardianSwimPower(condition),
                    (power, serializableData) -> serializableData.instance()
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                            MobOriginsMod.id("guardian_swim"),
                            DATA_FACTORY
                    );

    public GuardianSwimPower(Optional<EntityCondition> condition) {
        super(condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}