package me.ultrusmods.moborigins.mixin;

import me.ultrusmods.moborigins.power.MobOriginsPowers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Ravager.class)
public abstract class RavagerEntityMixin extends Monster {

    protected RavagerEntityMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Redirect(
            method = "roar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private List<LivingEntity> moborigins$filterAlignedEntities(
            Level level,
            Class<LivingEntity> entityClass,
            AABB box,
            Predicate<? super LivingEntity> predicate
    ) {
        List<LivingEntity> list = level.getEntitiesOfClass(entityClass, box, predicate);

        list.removeIf(entity ->
                MobOriginsPowers.hasPower(entity, MobOriginsPowers.PILLAGER_ALIGNED)
        );

        return list;
    }
}