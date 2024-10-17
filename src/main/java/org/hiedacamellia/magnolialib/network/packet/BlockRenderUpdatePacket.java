package org.hiedacamellia.magnolialib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(PacketFlow.CLIENTBOUND)
public record BlockRenderUpdatePacket(BlockPos pos) implements MagnoliaPacket {

    public static final Type<BlockRenderUpdatePacket> TYPE = new Type<>(MagnoliaLib.prefix("block_render_update"));

    public BlockRenderUpdatePacket(FriendlyByteBuf from) {
        this(BlockPos.of(from.readLong()));
    }

    public static final StreamCodec<ByteBuf, BlockRenderUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockRenderUpdatePacket::pos,
            BlockRenderUpdatePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
