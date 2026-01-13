package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BouncePower extends PowerType {

    public static final TypedDataObjectFactory<BouncePower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData()
                            .add("multiplier", SerializableDataTypes.DOUBLE, -0.85),
                    (data, condition) -> new BouncePower(
                            data.getDouble("multiplier"),
                            condition
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("multiplier", power.multiplier)
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                    MobOriginsMod.id("bouncy"),
                    DATA_FACTORY
            );

    private final double multiplier;

    public BouncePower(double multiplier, Optional<EntityCondition> condition) {
        super(condition);
        this.multiplier = multiplier;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
