package org.hiedacamellia.magnolialib.network.packet;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.MagnoliaTeamsClient;
import org.hiedacamellia.magnolialib.util.registry.Packet;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@Packet(PacketFlow.CLIENTBOUND)
public class SyncTeamMembersPacket implements MagnoliaPacket {
    public final Map<UUID, UUID> memberOf;

    public static final Type<SyncTeamMembersPacket> TYPE = new Type<>(MagnoliaLib.prefix("sync_team_members"));

    public SyncTeamMembersPacket(Map<UUID, UUID> memberOf) {
        this.memberOf = memberOf;
    }

    public SyncTeamMembersPacket (FriendlyByteBuf from) {
        memberOf = Maps.newHashMap();
        int size = from.readByte();
        IntStream.range(0, size).forEach(i ->
                memberOf.put(from.readUUID(), from.readUUID()));
    }

//    @Override
//    public void write(FriendlyByteBuf to) {
//        to.writeByte(memberOf.size());
//        memberOf.forEach((key, value) -> {
//            to.writeUUID(key);
//            to.writeUUID(value);
//        });
//    }

    @Override
    public void handleClient() {
        MagnoliaTeamsClient.setMembers(memberOf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
