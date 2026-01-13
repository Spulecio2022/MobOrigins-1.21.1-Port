package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.ResourcePowerType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

import me.ultrusmods.moborigins.data.MathOperation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ResourceMathPower extends PowerType {

    private final ResourceLocation toResource;
    private final ResourceLocation fromResource;
    private final MathOperation operation;

    public ResourceMathPower(ResourceLocation toResource,
                             ResourceLocation fromResource,
                             MathOperation operation) {
        super();
        this.toResource = toResource;
        this.fromResource = fromResource;
        this.operation = operation;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    public void applyMath(LivingEntity entity) {

        PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
        if (component == null) return;

        Set<Power> powers = component.getPowers(true);

        ResourcePowerType toType = null;
        ResourcePowerType fromType = null;
        Power toPower = null;

        for (Power p : powers) {
            PowerType type = component.getPowerType(p);

            if (type instanceof ResourcePowerType rpt) {
                if (type.getConfig().id().equals(toResource)) {
                    toType = rpt;
                    toPower = p;
                }
                if (type.getConfig().id().equals(fromResource)) {
                    fromType = rpt;
                }
            }
        }

        if (toType == null || fromType == null)
            return;

        int toVal = toType.getValue();
        int fromVal = fromType.getValue();

        int result = computeResult(toVal, fromVal, operation);

        toType.setValue(result);

        PowerHolderComponent.syncPower(entity, toPower);
    }

    public static final TypedDataObjectFactory<ResourceMathPower> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("to_resource", SerializableDataTypes.IDENTIFIER)
                            .add("from_resource", SerializableDataTypes.IDENTIFIER)
                            .add("operator", MathOperation.DATA_TYPE),
                    data -> new ResourceMathPower(
                            data.getId("to_resource"),
                            data.getId("from_resource"),
                            data.get("operator")
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("to_resource", power.toResource)
                            .set("from_resource", power.fromResource)
                            .set("operator", power.operation)
            );
    private int computeResult(int toVal, int fromVal, MathOperation operation) {
        return switch (operation) {
            case ADD       -> toVal + fromVal;
            case SUBTRACT  -> toVal - fromVal;
            case MULTIPLY  -> toVal * fromVal;
            case DIVIDE    -> fromVal != 0 ? toVal / fromVal : toVal;
            case MOD       -> fromVal != 0 ? toVal % fromVal : toVal;
            case EXPONENT  -> (int) Math.pow(toVal, fromVal);
            case SET       -> fromVal;
        };
    }

    public static final PowerConfiguration<ResourceMathPower> CONFIG =
            PowerConfiguration.of(
                    ResourceLocation.fromNamespaceAndPath("moborigins", "resource_math"),
                    DATA_FACTORY
            );
}