package me.ultrusmods.moborigins.action.bientity;

import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.core.Registry;

public class SetAngeredAtBiEntityAction {

    @SuppressWarnings("unchecked")
    public static void register() {

        ActionConfiguration<?> config = SetAngeredAtActionType.CONFIG;

        Registry.register(
                ApoliRegistries.BIENTITY_ACTION_TYPE,
                config.id(),
                (ActionConfiguration) config
        );
    }
}