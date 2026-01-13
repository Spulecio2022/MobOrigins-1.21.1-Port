package me.ultrusmods.moborigins.register;

import io.github.apace100.apoli.registry.ApoliRegistries;
import me.ultrusmods.moborigins.power.*;
import net.minecraft.core.Registry;

public class MobOriginsPowerTypes {

    public static void register() {

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ActionOnBreedAnimalPower.CONFIG.id(),
                ActionOnBreedAnimalPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ActionOnEntityTamePower.CONFIG.id(),
                ActionOnEntityTamePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                AddExperienceToResourcePower.CONFIG.id(),
                AddExperienceToResourcePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                CustomSleepPower.CONFIG.id(),
                CustomSleepPower.CONFIG
        );

        /*Registry.register(
                ApoliRegistries.POWER_TYPE,
                DyeableModelColorPower.CONFIG.id(),
                DyeableModelColorPower.CONFIG
        );*/

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                FogPower.CONFIG.id(),
                FogPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                IlluminatePower.CONFIG.id(),
                IlluminatePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                MimicEnchantPower.CONFIG.id(),
                MimicEnchantPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                PowderSnowPower.CONFIG.id(),
                PowderSnowPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                RemoveMobHostilityPower.CONFIG.id(),
                RemoveMobHostilityPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                RiptideOverridePower.CONFIG.id(),
                RiptideOverridePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ChannelingOverridePower.CONFIG.id(),
                ChannelingOverridePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                TotemChancePower.CONFIG.id(),
                TotemChancePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ModifyAttackDistanceScalingFactorPower.CONFIG.id(),
                ModifyAttackDistanceScalingFactorPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                BiomeModelColorPower.CONFIG.id(),
                BiomeModelColorPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                FallSoundPower.CONFIG.id(),
                FallSoundPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ModifyReputationPower.CONFIG.id(),
                ModifyReputationPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                BouncePower.CONFIG.id(),
                BouncePower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                SnowTrailPower.CONFIG.id(),
                SnowTrailPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                HostileAxolotlsPower.CONFIG.id(),
                HostileAxolotlsPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                GuardianSwimPower.CONFIG.id(),
                GuardianSwimPower.CONFIG
        );

        Registry.register(
                ApoliRegistries.POWER_TYPE,
                ElderGuardianSwimPower.CONFIG.id(),
                ElderGuardianSwimPower.CONFIG
        );
    }
}