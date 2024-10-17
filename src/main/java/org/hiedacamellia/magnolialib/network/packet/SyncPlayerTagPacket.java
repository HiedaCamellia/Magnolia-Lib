package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(value = PacketFlow.CLIENTBOUND)
public class SyncPlayerTagPacket extends SyncCompoundTagPacket {

    private final String tagName;

    public static final Type<SyncPlayerTagPacket> TYPE = new Type<>(MagnoliaLib.prefix("sync_player_tag"));
    public SyncPlayerTagPacket(String tagName, Player player) {
        super(player.getPersistentData().getCompound(tagName));
        this.tagName = tagName;
    }

    public SyncPlayerTagPacket(FriendlyByteBuf buf) {
        super(buf);
        tagName = buf.readUtf();
    }

//    @Override
//    public void write(FriendlyByteBuf buf) {
//        super.write(buf);
//        buf.writeUtf(tagName);
//    }


    @Override
    public void handle(Player player) {
        player.getPersistentData().put(tagName, tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
