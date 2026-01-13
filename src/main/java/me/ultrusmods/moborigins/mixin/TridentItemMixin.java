package me.ultrusmods.moborigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.RiptideOverridePower;
import me.ultrusmods.moborigins.power.MobOriginsPowers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.MoverType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Inject(method = "releaseUsing", at = @At("TAIL"))
    private void moborigins$riptideOverride(ItemStack stack, Level level, LivingEntity user, int timeLeft, CallbackInfo ci) {

        if (!(user instanceof Player player)) return;

        PowerHolderComponent component = PowerHolderComponent.getNullable(player);
        if (component == null) return;

        var powers = component.getPowers(true).stream()
                .filter(p -> p.getId().equals(MobOriginsPowers.RIPTIDE_OVERRIDE))
                .map(p -> (RiptideOverridePower) component.getPowerType(p))
                .toList();

        if (powers.isEmpty()) return;

        var enchantmentHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.RIPTIDE);

        int riptideLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
        if (riptideLevel <= 0) return;

        boolean override = powers.stream().anyMatch(RiptideOverridePower::isActive);
        if (!override && !player.isInWaterRainOrBubble()) return;

        int extraDamage = powers.stream().mapToInt(RiptideOverridePower::getTridentDamage).sum();

        if (!level.isClientSide()) {
            stack.hurtAndBreak(extraDamage, player, null);
        }

        float yaw = player.getYRot();
        float pitch = player.getXRot();

        float x = -Mth.sin(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);
        float y = -Mth.sin(pitch * (float)Math.PI / 180F);
        float z = Mth.cos(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);

        float magnitude = Mth.sqrt(x * x + y * y + z * z);
        float speed = 3.0F * ((1.0F + riptideLevel) / 4.0F);

        x *= speed / magnitude;
        y *= speed / magnitude;
        z *= speed / magnitude;

        player.push(x, y, z);
        player.startAutoSpinAttack(20, 0.6F, stack);

        if (player.onGround()) {
            player.move(MoverType.SELF, new Vec3(0.0D, 1.2D, 0.0D));
        }

        var sound = switch (riptideLevel) {
            case 3 -> SoundEvents.TRIDENT_RIPTIDE_3;
            case 2 -> SoundEvents.TRIDENT_RIPTIDE_2;
            default -> SoundEvents.TRIDENT_RIPTIDE_1;
        };

        level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}