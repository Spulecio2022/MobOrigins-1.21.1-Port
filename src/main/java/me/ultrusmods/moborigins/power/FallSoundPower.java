package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class FallSoundPower extends PowerType {

    private final int distance;
    private final SoundEvent smallSound;
    private final SoundEvent bigSound;

    public FallSoundPower(int distance, SoundEvent smallSound, SoundEvent bigSound) {
        this.distance = distance;
        this.smallSound = smallSound;
        this.bigSound = bigSound;
    }

    public int getDistance() {
        return distance;
    }

    public SoundEvent getBigSound() {
        return bigSound;
    }

    public SoundEvent getSmallSound() {
        return smallSound;
    }

    // -------------------------
    // DATA FACTORY
    // -------------------------
    public static final TypedDataObjectFactory<FallSoundPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("distance", SerializableDataTypes.INT, 4)
                            .add("small_fall_sound", SerializableDataTypes.SOUND_EVENT, SoundEvents.GENERIC_SMALL_FALL)
                            .add("big_fall_sound", SerializableDataTypes.SOUND_EVENT, SoundEvents.GENERIC_BIG_FALL),
                    data -> new FallSoundPower(
                            data.getInt("distance"),
                            data.get("small_fall_sound"),
                            data.get("big_fall_sound")
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("distance", power.distance)
                            .set("small_fall_sound", power.smallSound)
                            .set("big_fall_sound", power.bigSound)
            );

    // -------------------------
    // CONFIG
    // -------------------------
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("fall_sounds"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}