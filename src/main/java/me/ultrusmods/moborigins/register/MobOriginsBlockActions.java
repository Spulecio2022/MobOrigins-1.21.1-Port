package me.ultrusmods.moborigins.register;

import me.ultrusmods.moborigins.action.block.DecrementCauldronFluidAction;
import me.ultrusmods.moborigins.action.block.GrowBlockAction;

public class MobOriginsBlockActions {

    public static void register() {
        GrowBlockAction.register();
        DecrementCauldronFluidAction.register();
    }
}
