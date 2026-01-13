package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract boolean isSpectator();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void moborigins$interact(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (entity instanceof Player && MobOriginsPowers.hasPower(entity, MobOriginsPowers.RIDEABLE_CREATURE)) {
            Player player = (Player)(Object)this;

            if (!player.isVehicle() && !player.isSpectator()) {
                player.startRiding(entity);
                cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide));

                if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetPassengersPacket(entity));
                }
            } else {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    @ModifyArg(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            ),
            index = 0
    )
    private double moborigins$expandItemCollectorBoxX(double original) {
        if (MobOriginsPowers.hasPower((Player)(Object)this, MobOriginsPowers.ITEM_COLLECTOR)) {
            return original + 2.0;
        }
        return original;
    }

    @ModifyArg(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            ),
            index = 1
    )
    private double moborigins$expandItemCollectorBoxY(double original) {
        if (MobOriginsPowers.hasPower((Player)(Object)this, MobOriginsPowers.ITEM_COLLECTOR)) {
            return original + 2.0;
        }
        return original;
    }

    @ModifyArg(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            ),
            index = 2
    )
    private double moborigins$expandItemCollectorBoxZ(double original) {
        if (MobOriginsPowers.hasPower((Player)(Object)this, MobOriginsPowers.ITEM_COLLECTOR)) {
            return original + 2.0;
        }
        return original;
    }
}