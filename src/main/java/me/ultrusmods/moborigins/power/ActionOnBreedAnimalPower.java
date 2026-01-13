package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ActionOnBreedAnimalPower extends PowerType {

    public static final TypedDataObjectFactory<ActionOnBreedAnimalPower> DATA_FACTORY =
            PowerType.createConditionedDataFactory(
                    new SerializableData()
                            .add("bientity_action", BiEntityAction.DATA_TYPE.optional(), Optional.empty())
                            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty()),
                    (data, condition) -> new ActionOnBreedAnimalPower(
                            data.get("bientity_action"),
                            data.get("bientity_condition"),
                            condition
                    ),
                    (power, serializableData) -> serializableData.instance()
                            .set("bientity_action", power.biEntityAction)
                            .set("bientity_condition", power.biEntityCondition)
            );

    public static final PowerConfiguration<PowerType> CONFIG =
            (PowerConfiguration<PowerType>) (PowerConfiguration<?>)
                    new PowerConfiguration<>(
                            MobOriginsMod.id("action_on_breed_animal"),
                            DATA_FACTORY
                    );

    private final Optional<BiEntityAction> biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;

    public ActionOnBreedAnimalPower(Optional<BiEntityAction> biEntityAction,
                                    Optional<BiEntityCondition> biEntityCondition,
                                    @NotNull Optional<EntityCondition> condition) {
        super(condition);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return CONFIG;
    }

    /** Checks whether the power should run for this (player, child) pair */
    public boolean shouldExecute(Entity child) {
        Entity player = getHolder();
        return biEntityCondition.map(cond -> cond.test(player, child)).orElse(true);
    }

    /** Executes the bi-entity action on (player, child) */
    public void executeAction(Entity child) {
        Entity player = getHolder();
        biEntityAction.ifPresent(action -> action.execute(player, child));
    }
}