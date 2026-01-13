package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.ResourcePowerType;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;

import io.github.apace100.calio.data.SerializableData;

import me.ultrusmods.moborigins.MobOriginsMod;
import me.ultrusmods.moborigins.data.MathOperation;
import me.ultrusmods.moborigins.data.MobOriginsDataTypes;

import net.minecraft.core.Registry;

public class ResourceMathActionType extends EntityActionType {

    private final PowerType toResource;
    private final PowerType fromResource;
    private final MathOperation operation;

    public ResourceMathActionType(PowerType toResource, PowerType fromResource, MathOperation operation) {
        this.toResource = toResource;
        this.fromResource = fromResource;
        this.operation = operation;
    }

    private static Power findPower(PowerHolderComponent component, PowerType type) {
        return component.getPowers(true).stream()
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void accept(EntityActionContext ctx) {
        var entity = ctx.entity();

        PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
        if (component == null) return;

        Power to = findPower(component, toResource);
        Power from = findPower(component, fromResource);

        if (to == null || from == null) return;

        int toVal;
        int fromVal;

        if (toResource instanceof ResourcePowerType rpt) {
            toVal = rpt.getValue();
        } else if (toResource instanceof CooldownPowerType cpt) {
            toVal = cpt.getRemainingTicks();
        } else return;

        if (fromResource instanceof ResourcePowerType rpt) {
            fromVal = rpt.getValue();
        } else if (fromResource instanceof CooldownPowerType cpt) {
            fromVal = cpt.getRemainingTicks();
        } else return;

        int result = switch (operation) {
            case ADD       -> toVal + fromVal;
            case SUBTRACT  -> toVal - fromVal;
            case MULTIPLY  -> toVal * fromVal;
            case DIVIDE    -> fromVal != 0 ? toVal / fromVal : toVal;
            case MOD       -> fromVal != 0 ? toVal % fromVal : toVal;
            case SET       -> fromVal;
            case EXPONENT  -> (int) Math.pow(toVal, fromVal);
        };

        if (toResource instanceof ResourcePowerType rpt) {
            rpt.setValue(result);
            PowerHolderComponent.syncPower(entity, to);
        }

        if (toResource instanceof CooldownPowerType cpt) {
            cpt.setCooldown(result);
            PowerHolderComponent.syncPower(entity, to);
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static final TypedDataObjectFactory<EntityActionType> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("to_resource", ApoliDataTypes.POWER_REFERENCE)
                            .add("from_resource", ApoliDataTypes.POWER_REFERENCE)
                            .add("operator", MobOriginsDataTypes.MATH_OPERATOR),
                    data -> new ResourceMathActionType(
                            data.get("to_resource"),
                            data.get("from_resource"),
                            data.get("operator")
                    ),
                    (action, data) -> data.instance()
            );

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.of(
                    MobOriginsMod.id("resource_math"),
                    DATA_FACTORY
            );

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}