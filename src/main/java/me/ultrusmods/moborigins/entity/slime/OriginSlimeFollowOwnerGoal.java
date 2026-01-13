package me.ultrusmods.moborigins.entity.slime;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class OriginSlimeFollowOwnerGoal extends Goal {
    private final OriginSlimeEntity tameable;
    private LivingEntity owner;
    private final LevelReader world;
    private final double speed;
    private final PathNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPenalty;
    private final boolean leavesAllowed;

    public OriginSlimeFollowOwnerGoal(OriginSlimeEntity tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.tameable = tameable;
        this.world = tameable.level();
        this.speed = speed;
        this.navigation = tameable.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        if (!(this.navigation instanceof GroundPathNavigation) && !(this.navigation instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported navigation type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.tameable.getOwner();
        if (owner == null || owner.isSpectator()) {
            return false;
        }

        if (this.tameable.distanceToSqr(owner) < (double)(this.minDistance * this.minDistance)) {
            return false;
        }

        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        }

        return this.tameable.distanceToSqr(this.owner) > (double)(this.maxDistance * this.maxDistance);
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPenalty = this.tameable.getPathfindingMalus(PathType.WATER);
        this.tameable.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.tameable.setPathfindingMalus(PathType.WATER, this.oldWaterPenalty);
    }

    @Override
    public void tick() {
        this.tameable.getLookControl().setLookAt(this.owner, 10.0F, this.tameable.getMaxHeadXRot());

        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = 10;

            if (!this.tameable.isLeashed() && !this.tameable.isPassenger()) {
                if (this.tameable.distanceToSqr(this.owner) >= 144.0D) {
                    this.tryTeleport();
                } else {
                    this.navigation.moveTo(this.owner, this.speed);
                }
            }
        }
    }

    private void tryTeleport() {
        BlockPos ownerPos = this.owner.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int dx = this.getRandomInt(-3, 3);
            int dy = this.getRandomInt(-1, 1);
            int dz = this.getRandomInt(-3, 3);

            if (this.tryTeleportTo(ownerPos.getX() + dx, ownerPos.getY() + dy, ownerPos.getZ() + dz)) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0D && Math.abs(z - this.owner.getZ()) < 2.0D) {
            return false;
        }

        BlockPos pos = new BlockPos(x, y, z);
        if (!this.canTeleportTo(pos)) {
            return false;
        }

        this.tameable.moveTo(x + 0.5D, y, z + 0.5D, this.tameable.getYRot(), this.tameable.getXRot());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathType type = new WalkNodeEvaluator().getPathType(this.tameable, pos);

        if (type != PathType.WALKABLE) {
            return false;
        }

        BlockState below = this.world.getBlockState(pos.below());
        if (!this.leavesAllowed && below.getBlock() instanceof LeavesBlock) {
            return false;
        }

        BlockPos offset = pos.subtract(this.tameable.blockPosition());

        return this.world.isUnobstructed(
                this.tameable,
                net.minecraft.world.phys.shapes.Shapes.create(
                        this.tameable.getBoundingBox().move(offset)
                )
        );
    }

    private int getRandomInt(int min, int max) {
        return this.tameable.getRandom().nextInt(max - min + 1) + min;
    }
}
