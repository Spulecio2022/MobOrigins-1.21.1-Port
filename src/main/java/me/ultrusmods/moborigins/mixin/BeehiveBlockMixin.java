package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {

    @ModifyVariable(
            method = "angerNearbyBees",
            at = @At("STORE"),
            ordinal = 0 // first List<Player> stored
    )
    private List<Player> moborigins$filterQueenBeePlayers(List<Player> players) {

        players.removeIf(player ->
                MobOriginsPowers.hasPower(player, MobOriginsPowers.QUEEN_BEE)
        );

        return players;
    }
}