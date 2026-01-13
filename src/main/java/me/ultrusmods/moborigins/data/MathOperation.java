package me.ultrusmods.moborigins.data;

import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.util.StringRepresentable;

public enum MathOperation implements StringRepresentable {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    MOD,
    EXPONENT,
    SET;

    // Required for Apoli/Calio serialization
    public static final SerializableDataType<MathOperation> DATA_TYPE =
            SerializableDataType.enumValue(MathOperation.class);

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}