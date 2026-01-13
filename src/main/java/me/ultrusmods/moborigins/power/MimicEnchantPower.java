package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class MimicEnchantPower extends PowerType {

    private final Enchantment enchantment;
    private final int level;

    public MimicEnchantPower(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    // -------------------------
    // DATA FACTORY
    // -------------------------
    public static final TypedDataObjectFactory<MimicEnchantPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("enchantment", SerializableDataTypes.ENCHANTMENT, null)
                            .add("level", SerializableDataTypes.INT, 1),
                    data -> new MimicEnchantPower(
                            data.get("enchantment"),
                            data.getInt("level")
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("enchantment", power.enchantment)
                            .set("level", power.level)
            );

    // -------------------------
    // CONFIG
    // -------------------------
    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("mimic_enchant"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}
