package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

public class RiptideOverridePower extends PowerType {

    private final int tridentDamage;

    public RiptideOverridePower(int tridentDamage) {
        super();
        this.tridentDamage = tridentDamage;
    }

    public int getTridentDamage() {
        return tridentDamage;
    }

    // -------------------------
    // DATA FACTORY
    // -------------------------
    public static final TypedDataObjectFactory<RiptideOverridePower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("trident_damage", SerializableDataTypes.INT, 1),
                    data -> new RiptideOverridePower(data.getInt("trident_damage")),
                    (power, serializableData) -> serializableData.instance()
            );

    // -------------------------
    // CONFIG
    // -------------------------
    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("riptide_override"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}