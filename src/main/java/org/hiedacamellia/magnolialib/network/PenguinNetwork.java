package org.hiedacamellia.magnolialib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.hiedacamellia.magnolialib.network.packet.MagnoliaPacket;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeam;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeams;

import javax.annotation.Nullable;
import java.util.UUID;

public class PenguinNetwork {
    //TODO: Network Rework

//    public static void sendToClient(@Nullable ServerPlayer player,
//                                    CustomPacketPayload... packets) {
//        if (player != null) {
//            PacketDistributor.sendToAllPlayers(packets);
//        }
//    }

//    public static void sendToTeam(ServerLevel world, UUID uuid, MagnoliaPacket... packets) {
//        MagnoliaTeam team = MagnoliaTeams.getTeamFromID(world, uuid);
//        if (team != null) {
//            team.members().stream()
//                    .map(world::getPlayerByUUID)
//                    .filter(player -> player instanceof ServerPlayer)
//                    .map(player -> (ServerPlayer) player)
//                    .forEach(player -> sendToClient(player, packets));
//        }
//    }

//    public static void sendToServer(MagnoliaPacket packet) {
//        PacketDistributor.SERVER.noArg().send(packet);
//    }
//
//    public static void sendToEveryone(MagnoliaPacket packet) {
//        PacketDistributor.ALL.noArg().send(packet);
//    }

//    public static void sendToDimension(ServerLevel world, MagnoliaPacket... packets) {
//        PacketDistributor.DIMENSION.with(world.dimension()).send(packets);
//    }
//
//    public static void sendToNearby(ServerLevel world, double x, double y, double z, double distance, MagnoliaPacket... packets) {
//        PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(x, y, z, distance, world.dimension())).send(packets);
//    }

//    public static void sendToNearby(BlockEntity entity, MagnoliaPacket... packets) {
//        sendToNearby((ServerLevel) entity.getLevel(), entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ(), 64D, packets);
//    }
//
//    public static void sendToNearby(Entity entity, MagnoliaPacket... packets) {
//        sendToNearby((ServerLevel) entity.level(), entity.getX(), entity.getY(), entity.getZ(), 64D, packets);
//    }

//    public static <P extends MagnoliaPacket> void handlePacket(P packet, PlayPayloadContext context) {
//        context.player().ifPresent(player -> {
//            if (player instanceof ServerPlayer spl)
//                context.workHandler().submitAsync(() -> packet.handleServer(spl));
//            else context.workHandler().submitAsync(() -> packet.handleClient());
//        });
//    }

    public static <T extends ReloadableRegistry.PenguinRegistry<T>> T readRegistry(ReloadableRegistry<T> registry, FriendlyByteBuf buf) {
        return registry.getOrEmpty(buf.readResourceLocation());
    }

    public static <T extends ReloadableRegistry.PenguinRegistry<T>> void writeRegistry(T obj, FriendlyByteBuf buf) {
        buf.writeResourceLocation(obj.id());
    }
}
