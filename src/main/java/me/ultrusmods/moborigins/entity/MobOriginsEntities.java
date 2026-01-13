package me.ultrusmods.moborigins.entity;

import me.ultrusmods.moborigins.MobOriginsMod;
import me.ultrusmods.moborigins.entity.slime.OriginSlimeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class MobOriginsEntities {

    public static final EntityType<OriginSlimeEntity> ORIGIN_SLIME =
            Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MobOriginsMod.MOD_ID, "origin_slime"),
                    FabricEntityTypeBuilder.create(MobCategory.CREATURE, OriginSlimeEntity::new)
                            .dimensions(EntityDimensions.scalable(1.0F, 1.0F))
                            .trackRangeBlocks(10)
                            .build()
            );

    public static void init() {
        FabricDefaultAttributeRegistry.register(ORIGIN_SLIME, Monster.createMonsterAttributes());
    }
}
