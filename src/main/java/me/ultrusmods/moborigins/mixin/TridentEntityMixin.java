package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.ChannelingOverridePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends Projectile {

    protected TridentEntityMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void moborigins$channelingOverride(EntityHitResult hitResult, CallbackInfo ci) {

        Entity thrower = this.getOwner();
        if (thrower == null) return;

        if (this.level().isThundering()) return;

        PowerHolderComponent component = PowerHolderComponent.getNullable(thrower);
        if (component == null) return;

        component.getPowers(true).stream()
                .filter(p -> p.getId().equals(MobOriginsPowers.CHANNELING_OVERRIDE))
                .map(p -> (ChannelingOverridePower) component.getPowerType(p))
                .filter(ChannelingOverridePower::isActive)
                .forEach(power -> {

                    if (this.level() instanceof ServerLevel serverLevel) {

                        Entity hit = hitResult.getEntity();
                        BlockPos pos = hit.blockPosition();

                        if (serverLevel.canSeeSky(pos)) {

                            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                            if (lightning == null) return;

                            lightning.moveTo(Vec3.atBottomCenterOf(pos));

                            if (thrower instanceof ServerPlayer player) {
                                lightning.setCause(player);
                            }

                            serverLevel.addFreshEntity(lightning);
                        }
                    }
                });
    }
}