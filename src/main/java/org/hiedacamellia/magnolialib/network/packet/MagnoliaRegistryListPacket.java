package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;

import java.util.List;

public abstract class MagnoliaRegistryListPacket<O extends ReloadableRegistry.PenguinRegistry<O>> implements MagnoliaPacket {
    private final ReloadableRegistry<O> registry;
    private final List<ResourceLocation> registryNames;

    public MagnoliaRegistryListPacket(ReloadableRegistry<O> registry, List<O> entry) {
        this.registry = registry;
        this.registryNames = entry.stream().map(registry::getID).toList();
    }

    public MagnoliaRegistryListPacket(ReloadableRegistry<O> registry, FriendlyByteBuf buffer) {
        this.registry = registry;
        this.registryNames = buffer.readList(FriendlyByteBuf::readResourceLocation);
    }

//    @Override
//    public void write(FriendlyByteBuf pBuffer) {
//        pBuffer.writeCollection(registryNames, FriendlyByteBuf::writeResourceLocation);
//    }

    @Override
    public void handle(Player player) {
        handle(player, registryNames.stream().map(registry::get).toList());
    }

    protected abstract void handle(Player player, List<O> entries);
}
