package me.ultrusmods.moborigins.client.entity.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class OriginSlimeModel<T extends Entity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation OVER_LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("moborigins", "origin_slime_over"), "main");

    public static final ModelLayerLocation INNER_LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("moborigins", "origin_slime_inner"), "main");

    private final ModelPart root;

    public OriginSlimeModel(ModelPart root) {
        this.root = root;
    }

    // -----------------------------
    // Layer Definitions
    // -----------------------------

    public static LayerDefinition createOuterLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "cube",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, 16.0F, -4.0F, 8, 8, 8),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    public static LayerDefinition createInnerLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("cube",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.0F, 17.0F, -3.0F, 6, 6, 6),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("right_eye",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-3.25F, 18.0F, -3.5F, 2, 2, 2),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_eye",
                CubeListBuilder.create()
                        .texOffs(32, 4)
                        .addBox(1.25F, 18.0F, -3.5F, 2, 2, 2),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(32, 8)
                        .addBox(0.0F, 21.0F, -3.5F, 1, 1, 1),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    // -----------------------------
    // Layer Registration
    // -----------------------------

    public static void registerLayerDefinition() {
        EntityModelLayerRegistry.registerModelLayer(OVER_LAYER, OriginSlimeModel::createOuterLayer);
        EntityModelLayerRegistry.registerModelLayer(INNER_LAYER, OriginSlimeModel::createInnerLayer);
    }

    // -----------------------------
    // Animation + Root
    // -----------------------------

    @Override
    public void setupAnim(T entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        // No animation yet
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    // -----------------------------
    // REQUIRED for Mojang mappings
    // -----------------------------
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

}
