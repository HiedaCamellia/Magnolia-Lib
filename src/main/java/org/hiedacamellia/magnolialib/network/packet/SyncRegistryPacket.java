package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;

import java.util.Map;

@Packet(PacketFlow.CLIENTBOUND)
public class SyncRegistryPacket implements MagnoliaPacket {

    private final ReloadableRegistry<?> registry;
    private final Map<ResourceLocation, ? extends ReloadableRegistry.PenguinRegistry<?>> entries;

    public static final Type<SyncRegistryPacket> TYPE = new Type<>(MagnoliaLib.prefix("sync_penguin_registries"));

    public SyncRegistryPacket(ReloadableRegistry<?> registry) {
        this.registry = registry;
        this.entries = registry.registry();
    }

    public SyncRegistryPacket(FriendlyByteBuf buf) {
        String dir = buf.readUtf(32767);
        this.registry = ReloadableRegistry.REGISTRIES.stream().filter(r -> r.dir().equals(dir)).findFirst().orElse(null);
        assert registry != null;
        this.entries = buf.readMap(FriendlyByteBuf::readResourceLocation, (r) -> registry.emptyEntry().fromNetwork(r));
    }

//    @Override
//    public void write(FriendlyByteBuf to) {
//        to.writeUtf(registry.dir());
//        to.writeMap(entries, FriendlyByteBuf::writeResourceLocation, (buf, entry) -> entry.toNetwork(buf));
//    }

    @Override
    public void handle(Player player) {
        registry.set(entries);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}