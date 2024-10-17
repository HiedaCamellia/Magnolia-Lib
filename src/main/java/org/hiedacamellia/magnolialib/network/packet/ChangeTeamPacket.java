package org.hiedacamellia.magnolialib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.MagnoliaTeamsClient;
import org.hiedacamellia.magnolialib.util.registry.Packet;

import java.util.UUID;

@Packet(value = PacketFlow.CLIENTBOUND)
public record ChangeTeamPacket(UUID player, UUID oldTeam, UUID newTeam) implements MagnoliaPacket {

    public static final Type<ChangeTeamPacket> TYPE = new Type<>(MagnoliaLib.prefix("change_team"));

    public ChangeTeamPacket(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUUID(), buf.readUUID());
    }

    public static final StreamCodec<ByteBuf, ChangeTeamPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ChangeTeamPacket::player,
            UUIDUtil.STREAM_CODEC, ChangeTeamPacket::oldTeam,
            UUIDUtil.STREAM_CODEC, ChangeTeamPacket::newTeam,
            ChangeTeamPacket::new);


    @Override
    public void handle(Player clientPlayer) {
        MagnoliaTeamsClient.changeTeam(player, oldTeam, newTeam);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
