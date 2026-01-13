package me.ultrusmods.moborigins.action.block;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BlockActionContext;
import io.github.apace100.apoli.action.type.BlockActionType;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class DecrementCauldronFluidActionType extends BlockActionType {

    public static final ActionConfiguration<DecrementCauldronFluidActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("decrement_cauldron_fluid"),
                    DecrementCauldronFluidActionType::new
            );

    @Override
    public void accept(BlockActionContext context) {

        Level world = context.world();
        BlockPos pos = context.pos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof LayeredCauldronBlock) {
            LayeredCauldronBlock.lowerFillLevel(state, world, pos);
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }
}