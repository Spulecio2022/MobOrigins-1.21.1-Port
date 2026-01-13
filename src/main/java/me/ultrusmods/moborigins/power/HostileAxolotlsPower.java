package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

public class HostileAxolotlsPower extends PowerType {

    public HostileAxolotlsPower() {
        super();
    }

    public static final TypedDataObjectFactory<HostileAxolotlsPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData(),
                    data -> new HostileAxolotlsPower(),
                    (power, serializableData) -> serializableData.instance()
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("hostile_axolotls"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}