package me.ultrusmods.moborigins.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class MobOriginsPowers {

    public static final ResourceLocation STRONGER_SNOWBALLS =
            MobOriginsMod.id("stronger_snowballs");

    public static final ResourceLocation PILLAGER_ALIGNED =
            MobOriginsMod.id("pillager_aligned");

    public static final ResourceLocation BETTER_POTIONS =
            MobOriginsMod.id("better_potions");

    public static final ResourceLocation RIDEABLE_CREATURE =
            MobOriginsMod.id("rideable_creature_riding");

    public static final ResourceLocation QUEEN_BEE =
            MobOriginsMod.id("queen_bee");

    public static final ResourceLocation ITEM_COLLECTOR =
            MobOriginsMod.id("item_collector");

    public static final ResourceLocation CAREFUL_GATHERER =
            MobOriginsMod.id("careful_gatherer");

    public static final ResourceLocation BOUNCE =
            MobOriginsMod.id("bouncy");

    public static final ResourceLocation ENCHANTMENT =
            MobOriginsMod.id("mimic_enchant");

    public static final ResourceLocation ADD_EXPERIENCE_TO_RESOURCE =
            MobOriginsMod.id("add_experience_to_resource");

    public static final ResourceLocation TOTEM_CHANCE =
            MobOriginsMod.id("totem_chance");

    public static final ResourceLocation FALL_SOUNDS =
            MobOriginsMod.id("fall_sounds");

    public static final ResourceLocation WALK_ON_POWDER_SNOW =
            MobOriginsMod.id("walk_on_powder_snow");

    public static final ResourceLocation ACTION_ON_ENTITY_TAME =
            MobOriginsMod.id("action_on_entity_tame");

    public static final ResourceLocation REMOVE_MOB_HOSTILITY =
            MobOriginsMod.id("remove_mob_hostility");

    public static final ResourceLocation CHANNELING_OVERRIDE =
            MobOriginsMod.id("channeling_override");

    public static final ResourceLocation RIPTIDE_OVERRIDE =
            MobOriginsMod.id("riptide_override");

    public static final ResourceLocation GUARDIAN_SWIM =
            MobOriginsMod.id("guardian_swim");

    public static final ResourceLocation ELDER_GUARDIAN_SWIM =
            MobOriginsMod.id("elder_guardian_swim");

    public static boolean hasPower(Entity entity, ResourceLocation id) {

        PowerHolderComponent component = PowerHolderComponent.getNullable(entity);
        if (component == null) {
            return false;
        }

        return component.getPowers(true).stream()
                .anyMatch(power ->
                        power.getId().equals(id) &&
                                component.getPowerType(power).isActive()
                );
    }
}