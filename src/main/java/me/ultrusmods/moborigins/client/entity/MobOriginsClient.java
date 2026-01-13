package me.ultrusmods.moborigins.client.entity;

import me.ultrusmods.moborigins.client.entity.model.OriginSlimeModel;
import me.ultrusmods.moborigins.client.entity.render.OriginSlimeRenderer;
import me.ultrusmods.moborigins.entity.MobOriginsEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MobOriginsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Register the renderer
        EntityRendererRegistry.register(
                MobOriginsEntities.ORIGIN_SLIME,
                OriginSlimeRenderer::new
        );

        // Register the model layers
        OriginSlimeModel.registerLayerDefinition();
    }
}
