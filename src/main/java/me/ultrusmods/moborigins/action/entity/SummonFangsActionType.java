package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;

import io.github.apace100.apoli.registry.ApoliRegistries;
import me.ultrusmods.moborigins.MobOriginsMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.Mth;

public class SummonFangsActionType extends EntityActionType {

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("summon_fangs"),
                    SummonFangsActionType::new
            );

    @Override
    public void accept(EntityActionContext context) {
        Entity entity = context.entity();
        summonFangs(entity);
    }

    private void summonFangs(Entity entity) {
        double minY = entity.getY();
        double maxY = entity.getY() + 1;
        float baseYaw = entity.getYRot() + 180;
        int j;

        if (entity.isShiftKeyDown()) {
            for (j = 0; j < 5; ++j) {
                float h = baseYaw + j * Mth.PI * 0.4F;
                conjureFangs(entity,
                        entity.getX() + Mth.cos(h) * 1.5D,
                        entity.getZ() + Mth.sin(h) * 1.5D,
                        minY, maxY, h, 0);
            }

            for (j = 0; j < 8; ++j) {
                float h = baseYaw + j * Mth.PI * 2.0F / 8.0F + 1.2566371F;
                conjureFangs(entity,
                        entity.getX() + Mth.cos(h) * 2.5D,
                        entity.getZ() + Mth.sin(h) * 2.5D,
                        minY, maxY, h, 3);
            }
        } else {
            Vec3 vec = fromPolar(entity.getXRot(), entity.getYRot());
            for (j = 0; j < 16; ++j) {
                double dist = 1.25D * (j + 1);
                conjureFangs(entity,
                        entity.getX() + vec.x * dist,
                        entity.getZ() + vec.z * dist,
                        minY, maxY, baseYaw, j);
            }
        }
    }

    private void conjureFangs(Entity entity, double x, double z, double minY, double maxY, float yaw, int warmup) {
        BlockPos pos = BlockPos.containing(x, maxY, z);
        boolean found = false;
        double offsetY = 0.0D;

        while (pos.getY() >= Mth.floor(minY) - 1) {
            BlockPos below = pos.below();
            BlockState belowState = entity.level().getBlockState(below);

            if (belowState.isFaceSturdy(entity.level(), below, Direction.UP)) {
                if (!entity.level().isEmptyBlock(pos)) {
                    BlockState state = entity.level().getBlockState(pos);
                    VoxelShape shape = state.getCollisionShape(entity.level(), pos);
                    if (!shape.isEmpty()) {
                        offsetY = shape.max(Direction.Axis.Y);
                    }
                }
                found = true;
                break;
            }

            pos = pos.below();
        }

        if (found && entity instanceof LivingEntity living) {
            entity.level().addFreshEntity(
                    new EvokerFangs(
                            entity.level(),
                            x,
                            pos.getY() + offsetY,
                            z,
                            yaw,
                            warmup,
                            living
                    )
            );
        }
    }

    private Vec3 fromPolar(float pitch, float yaw) {
        float f = Mth.cos(-yaw * 0.017453292F - Mth.PI);
        float g = Mth.sin(-yaw * 0.017453292F - Mth.PI);
        float h = -Mth.cos(-pitch * 0.017453292F);
        float i = Mth.sin(-pitch * 0.017453292F);
        return new Vec3(g * h, i, f * h);
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}
