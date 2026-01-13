package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.BlockCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CustomSleepPower extends PowerType {

    public static final TypedDataObjectFactory<CustomSleepPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("block_condition", BlockCondition.DATA_TYPE.optional(), Optional.empty()),
                    data -> new CustomSleepPower(
                            data.get("block_condition")
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("block_condition", power.blockCondition)
            );

    private final Optional<BlockCondition> blockCondition;

    public CustomSleepPower(Optional<BlockCondition> blockCondition) {
        this.blockCondition = blockCondition;
    }

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("custom_sleep_block"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public boolean doesApply(Level world, BlockPos pos) {
        return blockCondition
                .map(cond -> cond.test(world, pos))
                .orElse(true);
    }
}