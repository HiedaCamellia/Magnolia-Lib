package org.hiedacamellia.magnolialib.client.gui.book.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.client.gui.PenguinFonts;
import org.hiedacamellia.magnolialib.client.gui.SimpleChatter;
import org.hiedacamellia.magnolialib.world.note.Note;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NoteWidget extends AbstractWidget {
    private static final Component EMPTY = Component.empty();
    private SimpleChatter chatter;
    private Note note;

    public NoteWidget(int x, int y, int w, int h, NoteWidget previous) {
        super(x, y, w, h, EMPTY);
        if (previous != null && previous.note != null)
            set(previous.note, previous.chatter);
    }

    @Nonnull
    @Override
    public Component getMessage() {
        return note.getTitle();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narration) {
        //TODO: Narration
    }

    public Note getNote() {
        return note;
    }

    public SimpleChatter getChatter() {
        return chatter;
    }

    public float getAlpha() {
        return alpha;
    }

    public void set(@Nonnull Note note, @Nullable SimpleChatter chatter) {
        this.note = note; //Update the stuff
        if (chatter == null) {
            this.chatter = new SimpleChatter(note.getNoteType().getText(note))
                    .withWidth(note.getNoteType().getTextWidth())
                    .withLines(note.getNoteType().getLineCount())
                    .withHeight(8).withFormatting(note.getNoteType().getTextFormatting())
                    .setInstant();
            this.chatter.update(PenguinFonts.UNICODE.get());
        } else this.chatter = chatter;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (note != null)
            note.getNoteType().render(graphics, this, mouseX, mouseY);
    }
}