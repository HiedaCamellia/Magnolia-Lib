package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(PacketFlow.CLIENTBOUND)
public record SetHeldItemPacket(InteractionHand hand, ItemStack stack) implements PenguinPacket {
    public static final ResourceLocation ID = MagnoliaLib.prefix("set_held_item");
    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    public SetHeldItemPacket(InteractionHand hand, ItemStack stack) {
        this.hand = hand;
        this.stack = stack;
    }

    public SetHeldItemPacket(FriendlyByteBuf from) {
        this(from.readEnum(InteractionHand.class), from.readItem());
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeEnum(hand);
        to.writeItem(stack);
    }

    @Override
    public void handle(Player player) {
        player.setItemInHand(hand, stack);
    }
}