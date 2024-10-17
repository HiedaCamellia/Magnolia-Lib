package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;

@Packet(value = PacketFlow.CLIENTBOUND)
public class TemplatePacket implements MagnoliaPacket {
    public static final ResourceLocation ID = MagnoliaLib.prefix("template_packet");
    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    public TemplatePacket() {

    }

    public TemplatePacket(FriendlyByteBuf buf) {

    }

    @Override
    public void write(FriendlyByteBuf to) {

    }


    @Override
    public void handle(Player player) {

    }
}