package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;

import java.util.Optional;

public class FogPower extends PowerType {

    public static final SerializableData DATA = new SerializableData()
            .add("red", SerializableDataTypes.FLOAT, 1F)
            .add("green", SerializableDataTypes.FLOAT, 1F)
            .add("blue", SerializableDataTypes.FLOAT, 1F)
            .add("start", SerializableDataTypes.INT, -5)
            .add("end", SerializableDataTypes.INT, 30);

    public static final PowerConfiguration<PowerType> CONFIG =
            PowerConfiguration.conditionedOf(
                    MobOriginsMod.id("fog"),
                    DATA,
                    FogPower::new,
                    (power, serializableData) -> {
                        FogPower fp = (FogPower) power;
                        SerializableData.Instance instance = serializableData.instance();
                        instance.set("red", fp.getRed());
                        instance.set("green", fp.getGreen());
                        instance.set("blue", fp.getBlue());
                        instance.set("start", fp.getStart());
                        instance.set("end", fp.getEnd());
                        return instance;
                    }
            );

    private final float red;
    private final float green;
    private final float blue;
    private final int start;
    private final int end;

    // ✔ EXACT signature required by TypedDataObjectFactory.simple
    public FogPower(SerializableData.Instance data, Optional<EntityCondition> condition) {
        super(condition);
        this.red = data.getFloat("red");
        this.green = data.getFloat("green");
        this.blue = data.getFloat("blue");
        this.start = data.getInt("start");
        this.end = data.getInt("end");
    }

    @Override
    public PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public float getRed() { return red; }
    public float getGreen() { return green; }
    public float getBlue() { return blue; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
}