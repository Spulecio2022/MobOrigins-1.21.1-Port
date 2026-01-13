package me.ultrusmods.moborigins.action.bientity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.BiEntityActionContext;
import io.github.apace100.apoli.action.type.BiEntityActionType;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import io.github.apace100.apoli.Apoli;

public class SetAngeredAtActionType extends BiEntityActionType {

    public static final ActionConfiguration<SetAngeredAtActionType> CONFIG =
            ActionConfiguration.simple(
                    MobOriginsMod.id("set_angered_at"),
                    SetAngeredAtActionType::new
            );

    @Override
    public void accept(BiEntityActionContext context) {
        Entity actor = context.actor();
        Entity target = context.target();

        if (target instanceof NeutralMob angerable && actor instanceof LivingEntity living) {
            angerable.setTarget(living);   // replaces setAngryAt()
            angerable.setRemainingPersistentAngerTime(100); // replaces setAngerTime()
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }
}