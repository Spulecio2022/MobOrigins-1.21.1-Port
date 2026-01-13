package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public abstract class SnowballEntityMixin extends ThrowableItemProjectile {

    public SnowballEntityMixin(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void moborigins$strongerSnowballs(EntityHitResult hit, CallbackInfo ci) {
        Snowball self = (Snowball)(Object)this;

        if (MobOriginsPowers.hasPower(self.getOwner(), MobOriginsPowers.STRONGER_SNOWBALLS)) {
            Entity target = hit.getEntity();

            target.hurt(
                    this.damageSources().thrown(self, self.getOwner()),
                    3.0F
            );
        }
    }
}
