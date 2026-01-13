package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

public class PowderSnowPower extends PowerType {

    public PowderSnowPower() {
        super();
    }

    public static final TypedDataObjectFactory<PowderSnowPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData(),
                    data -> new PowderSnowPower(),
                    (power, serializableData) -> null   // MUST return something
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("walk_on_powder_snow"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}