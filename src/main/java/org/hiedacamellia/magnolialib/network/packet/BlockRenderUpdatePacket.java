package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

import javax.annotation.Nonnull;

@Packet(PacketFlow.CLIENTBOUND)
public record BlockRenderUpdatePacket(BlockPos pos) implements PenguinPacket {
    public static final ResourceLocation ID = MagnoliaLib.prefix("block_render_update");

    @Override
    public @Nonnull ResourceLocation id() {
        return ID;
    }

    public BlockRenderUpdatePacket(FriendlyByteBuf from) {
        this(BlockPos.of(from.readLong()));
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeLong(pos.asLong());
    }
}
