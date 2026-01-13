package me.ultrusmods.moborigins.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.ultrusmods.moborigins.client.entity.model.OriginSlimeModel;
import me.ultrusmods.moborigins.entity.slime.OriginSlimeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

public class OriginSlimeEyesLayer<T extends OriginSlimeEntity>
        extends RenderLayer<T, OriginSlimeModel<T>> {

    public OriginSlimeEyesLayer(RenderLayerParent<T, OriginSlimeModel<T>> parent) {
        super(parent);
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

        if (entity.isInvisible()) return;

        VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(getTextureLocation(entity)));

        OriginSlimeModel<T> model = this.getParentModel();
        model.copyPropertiesTo(model);

        model.root().render(
                poseStack,
                consumer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F)
        );
    }
}
