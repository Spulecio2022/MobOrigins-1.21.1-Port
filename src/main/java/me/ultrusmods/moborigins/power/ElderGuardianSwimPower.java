package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ElderGuardianSwimPower extends PowerType {

    public static final TypedDataObjectFactory<ElderGuardianSwimPower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData(), // no fields needed
                    (data, condition) -> new ElderGuardianSwimPower(condition),
                    (power, serializableData) -> serializableData.instance()
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                            MobOriginsMod.id("elder_guardian_swim"),
                            DATA_FACTORY
                    );

    public ElderGuardianSwimPower(Optional<EntityCondition> condition) {
        super(condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}