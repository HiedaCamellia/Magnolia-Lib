package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.util.helper.PlayerHelper;

public interface MagnoliaPacket extends CustomPacketPayload {
    default void handleClient() {
        handle(PlayerHelper.getClient());
    }

    default void handleServer(ServerPlayer spl) {
        handle(spl);
    }

    default void handle(Player player) {}
}