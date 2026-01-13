package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.VariableIntPowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AddExperienceToResourcePower extends PowerType {

    public static final TypedDataObjectFactory<AddExperienceToResourcePower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData()
                            .add("resource", SerializableDataTypes.IDENTIFIER),
                    (data, condition) -> new AddExperienceToResourcePower(
                            data.getId("resource"),
                            condition
                    ),
                    (power, serializableData) -> serializableData.instance()
            );

    @SuppressWarnings("unchecked")
    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                            MobOriginsMod.id("add_experience_to_resource"),
                            DATA_FACTORY
                    );

    private final ResourceLocation resourceId;

    public AddExperienceToResourcePower(ResourceLocation resourceId,
                                        @NotNull Optional<EntityCondition> condition) {
        super(condition);
        this.resourceId = resourceId;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public int addToResource(int value) {
        Entity holder = getHolder();
        if (holder == null) return value;

        var component = PowerHolderComponent.getNullable(holder);
        if (component == null) return value;

        // Find the actual PowerType instance by ID
        PowerType resourceType = component.getPowerTypes().stream()
                .filter(p -> p.getConfig().id().equals(resourceId))
                .findFirst()
                .orElse(null);

        if (!(resourceType instanceof VariableIntPowerType vip)) {
            return value;
        }

        int amount = Math.min(vip.getMax() - vip.getValue(), value);
        vip.setValue(vip.getValue() + amount);

        PowerHolderComponent.sync(holder);

        return value - amount;
    }
}