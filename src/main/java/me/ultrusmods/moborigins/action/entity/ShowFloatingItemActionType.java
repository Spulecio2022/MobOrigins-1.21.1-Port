package me.ultrusmods.moborigins.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.network.codec.StreamCodec;
import me.ultrusmods.moborigins.MobOriginsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ShowFloatingItemActionType extends EntityActionType {

    private final ItemStack stack;

    public ShowFloatingItemActionType(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();

        // CLIENT: Only show to the local player
        if (entity.level().isClientSide()
                && entity instanceof Player player
                && Minecraft.getInstance().player != null
                && player.getUUID().equals(Minecraft.getInstance().player.getUUID())) {

            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        }

        // SERVER: Send packet to the correct player
        else if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundCustomPayloadPacket(new FloatingItemPayload(stack)));
        }
    }

    @Override
    public ActionConfiguration<?> getConfig() {
        return CONFIG;
    }

    public static final TypedDataObjectFactory<EntityActionType> DATA_FACTORY =
            TypedDataObjectFactory.simple(
                    new SerializableData()
                            .add("item_stack", SerializableDataTypes.ITEM_STACK),
                    data -> new ShowFloatingItemActionType(
                            data.get("item_stack")
                    ),
                    (action, data) -> data.instance()
            );

    public static final ActionConfiguration<EntityActionType> CONFIG =
            ActionConfiguration.of(
                    MobOriginsMod.id("show_floating_item"),
                    DATA_FACTORY
            );

    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, CONFIG.id(), CONFIG);
    }

    // ============================================================
    // Nested Payload Record (Mojmap 1.21.2+ Custom Payload System)
    // ============================================================

    public record FloatingItemPayload(ItemStack stack) implements CustomPacketPayload {

        public static final Type<FloatingItemPayload> TYPE =
                new Type<>(MobOriginsMod.id("floating_item"));

        public static final StreamCodec<FriendlyByteBuf, FloatingItemPayload> CODEC =
                StreamCodec.of(
                        (buf, payload) -> buf.writeNbt(
                                ItemStack.CODEC.encodeStart(
                                        net.minecraft.nbt.NbtOps.INSTANCE,
                                        payload.stack
                                ).result().orElseThrow()
                        ),
                        buf -> {
                            var nbt = buf.readNbt();
                            var stack = ItemStack.CODEC.parse(
                                    net.minecraft.nbt.NbtOps.INSTANCE,
                                    nbt
                            ).result().orElse(ItemStack.EMPTY);
                            return new FloatingItemPayload(stack);
                        }
                );

        @Override
        public Type<FloatingItemPayload> type() {
            return TYPE;
        }
    }
}