package me.ultrusmods.moborigins.data;

import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.world.BossEvent;

public class MobOriginsDataTypes {
    public static final SerializableDataType<BossEvent.BossBarColor> BOSS_BAR_COLORS =
            SerializableDataType.enumValue(BossEvent.BossBarColor.class);
    public static final SerializableDataType<BossEvent.BossBarOverlay> BOSS_BAR_STYLES =
            SerializableDataType.enumValue(BossEvent.BossBarOverlay.class);
    public static final SerializableDataType<MathOperation> MATH_OPERATOR = SerializableDataType.enumValue(MathOperation.class);
}
