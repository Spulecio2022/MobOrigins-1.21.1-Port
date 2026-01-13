package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SnowTrailPower extends PowerType {

    public SnowTrailPower() {
        super();
        this.setTicking(); // enable ticking
    }

    @Override
    public void serverTick() {
        LivingEntity entity = getHolder();

        BlockState snow = Blocks.SNOW.defaultBlockState();

        for (int l = 0; l < 4; ++l) {
            int i = Mth.floor(entity.getX() + ((l % 2 * 2 - 1) * 0.25F));
            int j = Mth.floor(entity.getY());
            int k = Mth.floor(entity.getZ() + ((l / 2 % 2 * 2 - 1) * 0.25F));

            BlockPos pos = new BlockPos(i, j, k);

            if (entity.level().getBlockState(pos).isAir() &&
                    snow.canSurvive(entity.level(), pos)) {
                entity.level().setBlock(pos, snow, 3);
            }
        }
    }

    public static final TypedDataObjectFactory<SnowTrailPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData(),
                    data -> new SnowTrailPower(),
                    (power, serializableData) -> serializableData.instance()
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("snow_trail"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}
