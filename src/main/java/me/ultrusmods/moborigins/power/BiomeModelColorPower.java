package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BiomeModelColorPower extends PowerType {

    public static final TypedDataObjectFactory<BiomeModelColorPower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData()
                            .add("alpha", ApoliDataTypes.NORMALIZED_FLOAT, 1.0F),
                    (data, condition) -> new BiomeModelColorPower(
                            data.get("alpha"),
                            condition
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("alpha", power.alpha)
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                    MobOriginsMod.id("biome_model_color"),
                    DATA_FACTORY
            );

    private final float alpha;

    public BiomeModelColorPower(float alpha, Optional<EntityCondition> condition) {
        super(condition);
        this.alpha = alpha;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    private int getGrassColor(Entity holder) {
        var client = Minecraft.getInstance();
        if (client.level == null) return 0xFFFFFF;

        var pos = holder.blockPosition();
        Biome biome = client.level.getBiome(pos).value();

        // Mojang mappings: grass color is computed directly from the biome
        return biome.getGrassColor(pos.getX(), pos.getZ());
    }

    public float getRed() {
        Entity holder = getHolder();
        if (holder == null || !holder.level().isClientSide()) return 1f;

        int color = getGrassColor(holder);
        return ((color >> 16) & 0xFF) / 255f;
    }

    public float getGreen() {
        Entity holder = getHolder();
        if (holder == null || !holder.level().isClientSide()) return 1f;

        int color = getGrassColor(holder);
        return ((color >> 8) & 0xFF) / 255f;
    }

    public float getBlue() {
        Entity holder = getHolder();
        if (holder == null || !holder.level().isClientSide()) return 1f;

        int color = getGrassColor(holder);
        return (color & 0xFF) / 255f;
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean isTranslucent() {
        return alpha < 1.0F;
    }
}