package org.hiedacamellia.magnolialib.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.world.block.entity.inventory.InventoryBlockEntity;

import javax.annotation.Nonnull;

@Packet(PacketFlow.CLIENTBOUND)
public record SetInventorySlotPacket(BlockPos pos, int slot, ItemStack stack) implements MagnoliaPacket {

    public static final Type<SetInventorySlotPacket> TYPE = new Type<>(MagnoliaLib.prefix("set_inventory_slot"));


    public static final StreamCodec<RegistryFriendlyByteBuf, SetInventorySlotPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SetInventorySlotPacket::pos,
            ByteBufCodecs.INT, SetInventorySlotPacket::slot,
            ItemStack.STREAM_CODEC, SetInventorySlotPacket::stack,
            SetInventorySlotPacket::new);

    @Override
    public void handle(Player player) {
        BlockEntity tile = player.level().getBlockEntity(pos);
        if (tile instanceof InventoryBlockEntity te) {
            te.setItem(slot, stack);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
