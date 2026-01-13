package me.ultrusmods.moborigins;

import com.mojang.logging.LogUtils;
import me.ultrusmods.moborigins.action.block.DecrementCauldronFluidAction;
import me.ultrusmods.moborigins.action.entity.ConsumeDyeColorAction;
import me.ultrusmods.moborigins.action.entity.SetDyeableModelColorAction;
import me.ultrusmods.moborigins.action.entity.SetItemCooldownAction;
import me.ultrusmods.moborigins.entity.MobOriginsEntities;
import me.ultrusmods.moborigins.event.SleepEvents;
import me.ultrusmods.moborigins.register.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class MobOriginsMod implements ModInitializer {
    public static final String MOD_ID = "moborigins";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        MobOriginsPowerTypes.register();
        MobOriginsEntityActionFactories.register();
        MobOriginsBiEntityActionFactories.register();
        MobOriginsBlockActions.register();

        // Register ALL custom entity actions
        ConsumeDyeColorAction.register();
        SetDyeableModelColorAction.register();
        DecrementCauldronFluidAction.register();
        SetItemCooldownAction.register();

        SleepEvents.init();
        MobOriginsEntities.init();

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
            var version = modContainer.getMetadata().getVersion().getFriendlyString();
            LOGGER.info("Mob Origins version " + version + " is loaded!");
        });
    }
}