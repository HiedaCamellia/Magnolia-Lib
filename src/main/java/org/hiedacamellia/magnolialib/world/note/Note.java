package org.hiedacamellia.magnolialib.world.note;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.data.PenguinRegistries;
import org.hiedacamellia.magnolialib.network.PenguinNetwork;
import org.hiedacamellia.magnolialib.network.packet.ReadNotePacket;
import org.hiedacamellia.magnolialib.network.packet.UnlockNotePacket;
import org.hiedacamellia.magnolialib.util.helper.PlayerHelper;
import org.hiedacamellia.magnolialib.util.icon.Icon;
import org.hiedacamellia.magnolialib.util.icon.ItemIcon;
import org.hiedacamellia.magnolialib.util.icon.SpriteIcon;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;
import org.hiedacamellia.magnolialib.world.note.type.NoteType;

public class Note implements ReloadableRegistry.PenguinRegistry<Note> {
    public static final Codec<Note> CODEC = RecordCodecBuilder.create(
            p_311728_ -> p_311728_.group(
                            ResourceLocation.CODEC.fieldOf("category").forGetter(inst -> inst.category),
                            //Optional Boolean? of "hidden"
                            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(inst -> inst.hidden),
                            Codec.BOOL.optionalFieldOf("locked", false).forGetter(inst -> inst.locked),
                            Icon.CODEC.optionalFieldOf("icon").forGetter(inst -> java.util.Optional.ofNullable(inst.icon)),
                            Codec.STRING.fieldOf("type").forGetter(inst -> inst.type.toString())
                    )
                    .apply(p_311728_, (category, hidden, locked, icon, type) -> {
                                Note note = new Note(category, PenguinRegistries.NOTE_TYPES.get(type));
                                if (hidden) note.setHidden();
                                if (locked) note.setLocked();
                                icon.ifPresent(note::setIcon);
                                return note;
                            }
                    ));

    public static final SpriteIcon NOTE_STANDARD = new SpriteIcon(new ResourceLocation(MagnoliaLib.MODID, "note_standard"));
    public static final SpriteIcon NOTE_HIDDEN = new SpriteIcon(new ResourceLocation(MagnoliaLib.MODID, "note_hidden"));
    private final ResourceLocation category;
    private final NoteType type;
    private Icon icon;
    private boolean locked;
    private boolean hidden;

    public Note(ResourceLocation category, NoteType type) {
        this.category = category;
        this.type = type;
        this.icon = ItemIcon.EMPTY;
    }

    public ResourceLocation id() {
        return PenguinRegistries.NOTES.getID(this);
    }

    public Component getTitle() {
        return Component.translatable("note." + id().getNamespace() + "." + id().getPath() + ".title");
    }

    public Component getText() {
        return Component.translatable("note." + id().getNamespace() + "." + id().getPath() + ".text");
    }

    public ResourceLocation getCategory() {
        return category;
    }

    public void setHidden() {
        this.hidden = true;
    }

    public void setLocked() {
        this.locked = true;
    }

    public Note setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isUnlocked(Player player) {
        return PlayerHelper.hasSubTag(player, "Notes", "Unlocked", id().toString());
    }

    public void unlock(Player player) {
        PlayerHelper.setSubTag(player, "Notes", "Unlocked", id().toString());
        if (player instanceof ServerPlayer sp)
            PenguinNetwork.sendToClient(sp, new UnlockNotePacket(this));
    }

    public boolean isRead(Player player) {
        return PlayerHelper.hasSubTag(player, "Notes", "Read", id().toString());
    }

    public void read(Player player) {
        PlayerHelper.setSubTag(player, "Notes", "Read", id().toString());
        if (player instanceof ServerPlayer sp)
            PenguinNetwork.sendToClient(sp, new ReadNotePacket(this));
    }

    public Icon getIcon() {
        return icon != null ? icon : hidden ? NOTE_HIDDEN : NOTE_STANDARD;
    }

    public boolean isDefault() {
        return !locked;
    }

    public NoteType getNoteType() {
        return type;
    }

    @Override
    public Note fromNetwork(FriendlyByteBuf buf) {
        Note note = new Note(buf.readResourceLocation(), PenguinRegistries.NOTE_TYPES.getOrDefault(buf.readUtf(), NoteType.TEXT));
        if (buf.readBoolean())
            note.setHidden();
        if (buf.readBoolean())
            note.setLocked();
        if (buf.readBoolean())
            note.setIcon(Icon.Type.values()[buf.readByte()].apply(buf));

        return note;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(category);
        buf.writeUtf(getNoteType().toString());
        buf.writeBoolean(hidden);
        buf.writeBoolean(locked);
        buf.writeBoolean(icon != null);
        if (icon != null) {
            buf.writeByte(icon.getType().ordinal());
            icon.toNetwork(buf);
        }
    }
}