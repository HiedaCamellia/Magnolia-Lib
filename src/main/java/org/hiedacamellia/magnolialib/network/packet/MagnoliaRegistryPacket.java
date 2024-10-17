package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;

public abstract class MagnoliaRegistryPacket<O extends ReloadableRegistry.PenguinRegistry<O>> implements MagnoliaPacket {
    private final ReloadableRegistry<O> registry;
    private final ResourceLocation registryName;

    public MagnoliaRegistryPacket(ReloadableRegistry<O> registry, O entry) {
        this.registry = registry;
        this.registryName = registry.getID(entry);
    }

    public MagnoliaRegistryPacket(ReloadableRegistry<O> registry, FriendlyByteBuf buffer) {
        this.registry = registry;
        this.registryName = buffer.readResourceLocation();
    }

//    @Override
//    public void write(FriendlyByteBuf pBuffer) {
//        pBuffer.writeResourceLocation(registryName);
//    }

    @Override
    public void handle(Player player) {
        handle(player, registry.get(registryName));
    }

    protected abstract void handle(Player player, O entry);
}
