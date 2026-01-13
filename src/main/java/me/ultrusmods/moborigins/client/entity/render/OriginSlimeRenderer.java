package me.ultrusmods.moborigins.client.entity.render;

import me.ultrusmods.moborigins.client.entity.model.OriginSlimeModel;
import me.ultrusmods.moborigins.entity.slime.OriginSlimeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import me.ultrusmods.moborigins.client.entity.layers.OriginSlimeOverLayer;
import me.ultrusmods.moborigins.client.entity.layers.OriginSlimeInnerLayer;
import me.ultrusmods.moborigins.client.entity.layers.OriginSlimeEyesLayer;
import me.ultrusmods.moborigins.client.entity.layers.OriginSlimeGelLayer;

@Environment(EnvType.CLIENT)
public class OriginSlimeRenderer extends MobRenderer<OriginSlimeEntity, OriginSlimeModel<OriginSlimeEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("moborigins", "textures/entity/slime/slime.png");

    public OriginSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new OriginSlimeModel<>(context.bakeLayer(OriginSlimeModel.OVER_LAYER)), 0.25F);

        this.addLayer(new OriginSlimeInnerLayer<>(this));
        this.addLayer(new OriginSlimeGelLayer<>(this));
        this.addLayer(new OriginSlimeEyesLayer<>(this));
        this.addLayer(new OriginSlimeOverLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(OriginSlimeEntity entity) {
        return TEXTURE;
    }
}
