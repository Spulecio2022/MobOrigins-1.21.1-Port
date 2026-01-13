package me.ultrusmods.moborigins.client.entity.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import me.ultrusmods.moborigins.client.entity.model.OriginSlimeModel;
import me.ultrusmods.moborigins.entity.slime.OriginSlimeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;

@Environment(EnvType.CLIENT)
public class OriginSlimeOverlayFeatureRenderer<T extends OriginSlimeEntity>
        extends RenderLayer<T, OriginSlimeModel<T>> {

    private final OriginSlimeModel<T> overlayModel;

    public OriginSlimeOverlayFeatureRenderer(RenderLayerParent<T, OriginSlimeModel<T>> parent,
                                             EntityRendererProvider.Context context) {
        super(parent);

        // Bake your custom OUTER layer
        ModelPart part = context.bakeLayer(OriginSlimeModel.OVER_LAYER);
        this.overlayModel = new OriginSlimeModel<>(part);
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       T entity,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        Minecraft mc = Minecraft.getInstance();
        boolean outline = mc.shouldEntityAppearGlowing(entity) && entity.isInvisible();

        if (!entity.isInvisible() || outline) {

            VertexConsumer consumer = outline
                    ? buffer.getBuffer(RenderType.outline(getTextureLocation(entity)))
                    : buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

            // Copy pose from main model
            this.getParentModel().copyPropertiesTo(this.overlayModel);

            // Animate
            this.overlayModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render with color tint
            this.overlayModel.renderToBuffer(
                    poseStack,
                    consumer,
                    packedLight,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                    entity.getRed(),
                    entity.getGreen(),
                    entity.getBlue(),
                    1.0F
            );
        }
    }
}
