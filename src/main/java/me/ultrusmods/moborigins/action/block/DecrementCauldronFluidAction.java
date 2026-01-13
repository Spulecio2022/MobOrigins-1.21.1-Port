package me.ultrusmods.moborigins.action.block;

import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.action.ActionConfiguration;
import net.minecraft.core.Registry;

public class DecrementCauldronFluidAction {

    @SuppressWarnings("unchecked")
    public static void register() {

        ActionConfiguration<?> config = DecrementCauldronFluidActionType.CONFIG;

        Registry.register(
                ApoliRegistries.BLOCK_ACTION_TYPE,
                config.id(),
                (ActionConfiguration) config
        );
    }
}