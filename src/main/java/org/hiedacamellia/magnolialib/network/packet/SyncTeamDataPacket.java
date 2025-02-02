package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.MagnoliaTeamsClient;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(PacketFlow.CLIENTBOUND)
public class SyncTeamDataPacket extends SyncCompoundTagPacket {

    public static final Type<SyncTeamDataPacket> TYPE = new Type<>(MagnoliaLib.prefix("sync_team_data"));

    public SyncTeamDataPacket(CompoundTag tag) {
        super(tag);
    }

    public SyncTeamDataPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(Player player) {
        MagnoliaTeamsClient.setInstance(tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}