package org.hiedacamellia.magnolialib.network.packet;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(PacketFlow.CLIENTBOUND)
public record SetHeldItemPacket(InteractionHand hand, ItemStack stack) implements MagnoliaPacket {

    public static final Type<SetActiveStatePacket> TYPE = new Type<>(MagnoliaLib.prefix("set_held_item"));

//
//    public static final StreamCodec<ByteBuf, SetHeldItemPacket> STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.INT, SetHeldItemPacket::hand,
//            ItemStack.STREAM_CODEC, SetHeldItemPacket::stack,
//            SetHeldItemPacket::new);


    @Override
    public void handle(Player player) {
        player.setItemInHand(hand, stack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}