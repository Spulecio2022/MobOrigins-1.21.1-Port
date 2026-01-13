package me.ultrusmods.moborigins.register;

import me.ultrusmods.moborigins.action.entity.*;

public class MobOriginsEntityActionFactories {

    public static void register() {

        ConsumeDyeColorAction.register();
        // CopyResourceAction.register();  // if you re-enable it later

        SetFreezeTicksAction.register();
        ShowFloatingItemAction.register();
        SetDyeableModelColorAction.register();
        ResourceMathAction.register();
        JumpAction.register();
        SummonFangsAction.register();
        DamageEquipmentAction.register();
        SummonSlimeAction.register();
        SetItemCooldownAction.register();
    }
}
