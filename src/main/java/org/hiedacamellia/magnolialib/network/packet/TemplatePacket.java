package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(value = PacketFlow.CLIENTBOUND)
public class TemplatePacket implements MagnoliaPacket {


    public static final Type<SyncRegistryPacket> TYPE = new Type<>(MagnoliaLib.prefix("template_packet"));

    public TemplatePacket() {

    }

    public TemplatePacket(FriendlyByteBuf buf) {

    }


    @Override
    public void handle(Player player) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}