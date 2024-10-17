package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class SyncCompoundTagPacket implements MagnoliaPacket {
    public CompoundTag tag;

    public SyncCompoundTagPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public SyncCompoundTagPacket(final FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }
//
//    @Override
//    public void write(FriendlyByteBuf buf) {
//        buf.writeNbt(tag);
//    }
}