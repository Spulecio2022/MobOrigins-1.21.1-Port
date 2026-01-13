package me.ultrusmods.moborigins.event;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.power.CustomSleepPower;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;

import java.util.List;

public class SleepEvents {
    public static void init() {

        // When player right-clicks a block
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClientSide()) {

                PowerHolderComponent component = PowerHolderComponent.getNullable(player);
                List<CustomSleepPower> powers = component.getPowerTypes().stream()
                        .filter(type -> type instanceof CustomSleepPower)
                        .map(type -> (CustomSleepPower) type)
                        .toList();

                for (CustomSleepPower customSleepPower : powers) {
                    if (customSleepPower.doesApply(world, hitResult.getBlockPos())) {
                        player.startSleepInBed(hitResult.getBlockPos()).ifLeft(reason -> {
                            if (reason != null) {
                                player.displayClientMessage(
                                        Component.literal("You cannot sleep right now: " + reason.name()),
                                        true
                                );
                            }
                        });
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
        });

        // Modify sleeping direction
        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
            if (entity instanceof Player playerEntity) {

                PowerHolderComponent component = PowerHolderComponent.getNullable(playerEntity);
                List<CustomSleepPower> powers = component.getPowerTypes().stream()
                        .filter(type -> type instanceof CustomSleepPower)
                        .map(type -> (CustomSleepPower) type)
                        .toList();

                for (CustomSleepPower customSleepPower : powers) {
                    if (customSleepPower.doesApply(playerEntity.level(), sleepingPos)) {
                        return Direction.NORTH;
                    }
                }
            }
            return sleepingDirection;
        });

        // Allow bed use
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            if (entity instanceof Player playerEntity) {

                PowerHolderComponent component = PowerHolderComponent.getNullable(playerEntity);
                List<CustomSleepPower> powers = component.getPowerTypes().stream()
                        .filter(type -> type instanceof CustomSleepPower)
                        .map(type -> (CustomSleepPower) type)
                        .toList();

                for (CustomSleepPower customSleepPower : powers) {
                    if (customSleepPower.doesApply(playerEntity.level(), sleepingPos)) {
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
        });

        // Allow resetting time
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {

            var sleepPos = player.getSleepingPos();
            if (sleepPos.isPresent()) {

                PowerHolderComponent component = PowerHolderComponent.getNullable(player);
                List<CustomSleepPower> powers = component.getPowerTypes().stream()
                        .filter(type -> type instanceof CustomSleepPower)
                        .map(type -> (CustomSleepPower) type)
                        .toList();

                if (player.level().isDay()) {
                    return false;
                }

                for (CustomSleepPower customSleepPower : powers) {
                    if (customSleepPower.doesApply(player.level(), sleepPos.get())) {
                        return true;
                    }
                }
            }
            return true;
        });

        // Modify wake-up position
        EntitySleepEvents.MODIFY_WAKE_UP_POSITION.register((entity, sleepingPos, bedState, wakeUpPos) -> {

            PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
            List<CustomSleepPower> powers = component.getPowerTypes().stream()
                    .filter(type -> type instanceof CustomSleepPower)
                    .map(type -> (CustomSleepPower) type)
                    .toList();

            for (CustomSleepPower customSleepPower : powers) {
                if (customSleepPower.doesApply(entity.level(), sleepingPos)) {
                    return DismountHelper.findSafeDismountLocation(
                            entity.getType(),
                            entity.level(),
                            sleepingPos,
                            true
                    );
                }
            }
            return wakeUpPos;
        });
    }
}