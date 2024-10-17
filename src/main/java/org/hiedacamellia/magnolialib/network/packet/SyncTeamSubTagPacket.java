package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.PenguinTeamsClient;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(value = PacketFlow.CLIENTBOUND)
public class SyncTeamSubTagPacket extends SyncCompoundTagPacket {
    public static final ResourceLocation ID = MagnoliaLib.prefix("sync_team_sub_tag");
    private final String tagName;

    public SyncTeamSubTagPacket(String tagName, CompoundTag tag) {
        super(tag);
        this.tagName = tagName;
    }

    public SyncTeamSubTagPacket(FriendlyByteBuf buf) {
        super(buf);
        tagName = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeUtf(tagName);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        PenguinTeamsClient.setTag(tagName, tag);
    }
}
