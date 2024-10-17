package org.hiedacamellia.magnolialib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.world.block.entity.machine.MachineBlockEntity;
import org.hiedacamellia.magnolialib.world.note.Note;

import javax.annotation.Nonnull;

@Packet(PacketFlow.CLIENTBOUND)
public record SetActiveStatePacket(BlockPos pos, boolean active) implements MagnoliaPacket {

    public static final Type<SetActiveStatePacket> TYPE = new Type<>(MagnoliaLib.prefix("set_active_state"));

    public SetActiveStatePacket(FriendlyByteBuf from) {
        this(from.readBlockPos(), from.readBoolean());
    }

    public static final StreamCodec<ByteBuf, SetActiveStatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SetActiveStatePacket::pos,
            ByteBufCodecs.BOOL, SetActiveStatePacket::active,
            SetActiveStatePacket::new);
//    @Override
//    public void write(FriendlyByteBuf to) {
//        to.writeBlockPos(pos);
//        to.writeBoolean(active);
//    }

    @Override
    public void handle(Player player) {
        BlockEntity tile = player.level().getBlockEntity(pos);
        if (tile instanceof MachineBlockEntity) {
            ((MachineBlockEntity)tile).setState(active);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
