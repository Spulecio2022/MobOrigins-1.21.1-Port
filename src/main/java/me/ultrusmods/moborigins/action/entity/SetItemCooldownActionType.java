package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;

import me.ultrusmods.moborigins.MobOriginsMod;

import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SetItemCooldownActionType extends EntityActionType {

    private final InteractionHand hand;
    private final int ticks;
    private final net.minecraft.world.item.Item item;

    public SetItemCooldownActionType(InteractionHand hand, int ticks, net.minecraft.world.item.Item item) {
        this.hand = hand;
        this.ticks = ticks;
        this.item = item;
    }

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();

        if (entity instanceof Player player) {

            if (hand != null) {
                player.getCooldowns().addCooldown(
                        player.getItemInHand(hand).getItem(),
                        ticks
                );
            } else if (item != null) {
                player.getCooldowns().addCooldown(item, ticks);
            }
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static final TypedDataObjectFactory<EntityActionType> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("item", SerializableDataTypes.ITEM, null)
                            .add("ticks", SerializableDataTypes.INT)
                            .add("hand", SerializableDataTypes.HAND, null),
                    data -> new SetItemCooldownActionType(
                            data.get("hand"),
                            data.getInt("ticks"),
                            data.get("item")
                    ),
                    (action, data) -> data.instance()
            );

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.of(
                    MobOriginsMod.id("set_item_cooldown"),
                    DATA_FACTORY
            );

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }
}
