package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ModifyAttackDistanceScalingFactorPower extends ValueModifyingPowerType {

    private final BiEntityCondition biEntityCondition;

    public ModifyAttackDistanceScalingFactorPower(
            BiEntityCondition biEntityCondition,
            List<Modifier> modifiers
    ) {
        super();
        this.biEntityCondition = biEntityCondition;

        if (modifiers != null) {
            modifiers.forEach(this::addModifier);
        }
    }

    public boolean doesApply(Entity other, LivingEntity holder) {
        return biEntityCondition == null || biEntityCondition.test(other, holder);
    }

    public static final TypedDataObjectFactory<ModifyAttackDistanceScalingFactorPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), null)
                            .add("modifier", Modifier.DATA_TYPE, null)
                            .add("modifiers", Modifier.LIST_TYPE, null),
                    data -> {
                        BiEntityCondition cond = data.get("bientity_condition");

                        List<Modifier> mods = new ArrayList<>();
                        Modifier single = data.get("modifier");
                        List<Modifier> list = data.get("modifiers");

                        if (list != null) mods.addAll(list);
                        if (single != null) mods.add(single);

                        return new ModifyAttackDistanceScalingFactorPower(cond, mods);
                    },
                    (powerType, serializableData) -> serializableData.instance()
                            .set("bientity_condition", powerType.biEntityCondition)
                            .set("modifiers", powerType.getModifiers())
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    PowerConfiguration.of(
                            MobOriginsMod.id("modify_attack_distance_scale"),
                            DATA_FACTORY
                    );

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }
}