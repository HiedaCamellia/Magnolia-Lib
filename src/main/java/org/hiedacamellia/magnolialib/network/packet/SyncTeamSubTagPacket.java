package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.MagnoliaTeamsClient;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(value = PacketFlow.CLIENTBOUND)
public class SyncTeamSubTagPacket extends SyncCompoundTagPacket {

    private final String tagName;

    public static final Type<SyncTeamSubTagPacket> TYPE = new Type<>(MagnoliaLib.prefix("sync_team_sub_tag"));

    public SyncTeamSubTagPacket(String tagName, CompoundTag tag) {
        super(tag);
        this.tagName = tagName;
    }

    public SyncTeamSubTagPacket(FriendlyByteBuf buf) {
        super(buf);
        tagName = buf.readUtf();
    }

//    @Override
//    public void write(FriendlyByteBuf buf) {
//        super.write(buf);
//        buf.writeUtf(tagName);
//    }


    @Override
    public void handleClient() {
        MagnoliaTeamsClient.setTag(tagName, tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
