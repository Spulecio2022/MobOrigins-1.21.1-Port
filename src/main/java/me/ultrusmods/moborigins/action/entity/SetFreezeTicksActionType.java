package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.core.Registry;

public class SetFreezeTicksActionType extends EntityActionType {

    private final int ticks;

    public SetFreezeTicksActionType(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void accept(EntityActionContext context) {
        context.entity().setTicksFrozen(ticks);
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static final TypedDataObjectFactory<EntityActionType> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("ticks", SerializableDataTypes.INT, 20),
                    data -> new SetFreezeTicksActionType(
                            data.getInt("ticks")
                    ),
                    (action, data) -> data.instance()
                            .set("ticks", ((SetFreezeTicksActionType) action).ticks)
            );

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.of(
                    MobOriginsMod.id("set_freeze_ticks"),
                    DATA_FACTORY
            );

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}
