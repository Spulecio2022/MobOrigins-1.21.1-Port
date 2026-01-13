package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntitySwimMixin extends Entity {

    @Unique
    private boolean moborigins$wasInWater = false;

    public LivingEntitySwimMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void moborigins$applyGuardianSwimLogic(Vec3 travelVector, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;

        // Power detection
        var component = PowerHolderComponent.getNullable(self);
        if (component == null) return;

        boolean isGuardian = MobOriginsPowers.hasPower(self, MobOriginsPowers.GUARDIAN_SWIM);
        boolean isElderGuardian = MobOriginsPowers.hasPower(self, MobOriginsPowers.ELDER_GUARDIAN_SWIM);

        if (!isGuardian && !isElderGuardian) {
            moborigins$wasInWater = self.isInWaterOrBubble();
            return;
        }

        boolean swimming = self.isSwimming();
        boolean inWater = self.isInWaterOrBubble();
        boolean sprinting = self.isSprinting();

        Vec3 velocity = self.getDeltaMovement();

        // Swim boost (strong)
        if (swimming) {
            double boost = isElderGuardian ? 1.0 : 0.8; // Elder Guardian swims faster
            double m = 1.0 + boost;
            velocity = velocity.multiply(m, m, m);

            // Optional sprint-swim bonus
            if (sprinting) {
                velocity = velocity.multiply(1.15, 1.15, 1.15);
            }
        }
        // General water boost (gentle)
        else if (inWater) {
            double boost = isElderGuardian ? 0.5 : 0.35;
            velocity = velocity.multiply(1.35, 1.35, 1.35);
        }

        // Momentum cutoff when leaving water
        if (!inWater && moborigins$wasInWater) {
            velocity = new Vec3(velocity.x * 0.2, velocity.y, velocity.z * 0.2);
        }

        self.setDeltaMovement(velocity);
        moborigins$wasInWater = inWater;
    }
}
