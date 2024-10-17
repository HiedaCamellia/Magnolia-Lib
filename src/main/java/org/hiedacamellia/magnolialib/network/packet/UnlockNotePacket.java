package org.hiedacamellia.magnolialib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.data.PenguinRegistries;
import org.hiedacamellia.magnolialib.network.PenguinNetwork;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.world.note.Note;

@Packet(value = PacketFlow.CLIENTBOUND)
public record UnlockNotePacket(Note note) implements MagnoliaPacket {

    public static final Type<UnlockNotePacket> TYPE = new Type<>(MagnoliaLib.prefix("unlock_note"));


    public static final StreamCodec<ByteBuf, UnlockNotePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Note.CODEC), UnlockNotePacket::note,
            UnlockNotePacket::new);

    @Override
    public void handle(Player player) {
        note.unlock(player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}