package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IlluminatePower extends PowerType {

    public static final SerializableData DATA = new SerializableData()
            .add("light", SerializableDataTypes.INT, 15);

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.conditionedOf(
                            MobOriginsMod.id("illuminate"),
                            DATA,
                            IlluminatePower::new,
                            (power, serializableData) -> {
                                SerializableData.Instance instance = serializableData.instance();
                                instance.set("light", ((IlluminatePower) power).getLight());
                                return instance;
                            }
                    );

    private final int light;

    public IlluminatePower(SerializableData.Instance data, @NotNull Optional<EntityCondition> condition) {
        super(condition);
        this.light = Math.max(0, Math.min(15, data.getInt("light")));
    }

    @Override
    public PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public int getLight() {
        return light;
    }
}