package me.ultrusmods.moborigins.mixin.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.IlluminatePower;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRendererMixin {

    @Inject(
            method = "getPackedLightCoords",
            at = @At("HEAD"),
            cancellable = true
    )
    private void moborigins$overrideEntityLight(Entity entity, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
        if (component == null) return;

        int illuminate = component.getPowerTypes().stream()
                .filter(p -> p instanceof IlluminatePower)
                .map(p -> (IlluminatePower) p)
                .mapToInt(IlluminatePower::getLight)
                .max()
                .orElse(0);

        if (illuminate <= 0) return;

        // Clamp to valid range
        int blockLight = Math.min(illuminate, 15);

        // Get vanilla packed light
        int vanilla = ((EntityRenderDispatcher)(Object)this).getPackedLightCoords(entity, tickDelta);

        // Extract sky light (upper 16 bits)
        int sky = (vanilla >> 16) & 0xFFFF;

        // Repack: block light in low bits, sky light preserved
        int packed = (blockLight << 4) | (sky << 16);

        cir.setReturnValue(packed);
    }
}
