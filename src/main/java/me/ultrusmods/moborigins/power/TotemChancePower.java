package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

public class TotemChancePower extends PowerType {

    private final float breakChance;

    public TotemChancePower(float breakChance) {
        super();
        this.breakChance = breakChance;
    }

    public float getBreakChance() {
        return breakChance;
    }

    // -------------------------
    // DATA FACTORY
    // -------------------------
    public static final TypedDataObjectFactory<TotemChancePower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("chance", SerializableDataTypes.FLOAT, 0.1F),
                    data -> new TotemChancePower(data.getFloat("chance")),
                    (power, serializableData) -> serializableData.instance()
            );

    // -------------------------
    // CONFIG
    // -------------------------
    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("totem_chance"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}