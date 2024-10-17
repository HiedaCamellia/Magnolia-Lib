package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.data.PenguinRegistries;
import org.hiedacamellia.magnolialib.network.PenguinNetwork;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.world.note.Note;

@Packet(value = PacketFlow.CLIENTBOUND)
public record UnlockNotePacket(Note note) implements MagnoliaPacket {
    public static final ResourceLocation ID = MagnoliaLib.prefix("unlock_note");
    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    public UnlockNotePacket(FriendlyByteBuf buf) {
        this(PenguinNetwork.readRegistry(PenguinRegistries.NOTES, buf));
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        PenguinNetwork.writeRegistry(note, buf);
    }

    @Override
    public void handle(Player player) {
        note.unlock(player);
    }
}