package me.ultrusmods.moborigins.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.MobOriginsPowers;
import me.ultrusmods.moborigins.power.ModifyReputationPower;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.VillagerData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends Entity {

    public VillagerEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow public abstract VillagerData getVillagerData();

    @ModifyReturnValue(method = "getPlayerReputation", at = @At("RETURN"))
    private int moborigins$modifyReputation(int original, Player player) {

        var modifiers = new ArrayList<io.github.apace100.apoli.util.modifier.Modifier>();

        PowerHolderComponent.getPowerTypes(player, ModifyReputationPower.class)
                .stream()
                .filter(p -> p.doesApply((Entity)(Object)this, player))
                .forEach(p -> modifiers.addAll(p.getModifiers()));

        double modified = io.github.apace100.apoli.util.modifier.ModifierUtil.applyModifiers(player, modifiers, original);

        return (int)Math.round(modified);
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void moborigins$illagerLootOnDeath(DamageSource source, CallbackInfo ci) {

        if (!(source.getEntity() instanceof Player player)) return;
        if (!MobOriginsPowers.hasPower(player, MobOriginsPowers.PILLAGER_ALIGNED)) return;
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        Villager villager = (Villager)(Object)this;
        String profession = villager.getVillagerData().getProfession().toString();

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("moborigins", "illager/" + profession);
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, id);

        LootTable lootTable = serverLevel.getServer()
                .reloadableRegistries()
                .getLootTable(key);

        LootParams.Builder params = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, villager)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withParameter(LootContextParams.ORIGIN, villager.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source);

        LootParams built = params.create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENTITY);

        List<ItemStack> loot = lootTable.getRandomItems(built);

        for (ItemStack stack : loot) {
            this.spawnAtLocation(stack);
        }
    }
}