package me.ultrusmods.moborigins.action.block;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;

import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.server.level.ServerLevel;

public class GrowBlockActionType extends BlockActionType {

    public static final ActionConfiguration<GrowBlockActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("grow"),
                    GrowBlockActionType::new
            );

    @Override
    public void accept(BlockActionContext context) {

        Level world = context.world();
        BlockPos pos = context.pos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BonemealableBlock fertilizable) {
            if (fertilizable.isValidBonemealTarget(world, pos, state)
                    && fertilizable.isBonemealSuccess(world, world.random, pos, state)
                    && world instanceof ServerLevel serverWorld) {

                fertilizable.performBonemeal(serverWorld, world.random, pos, state);
            }
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }
}