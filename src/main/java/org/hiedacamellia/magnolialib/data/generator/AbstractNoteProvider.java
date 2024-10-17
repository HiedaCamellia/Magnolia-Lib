package org.hiedacamellia.magnolialib.data.generator;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.world.note.Category;
import org.hiedacamellia.magnolialib.world.note.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractNoteProvider implements DataProvider {
    private final PackOutput.PathProvider categoryPathProvider;
    private final PackOutput.PathProvider notePathProvider;
    private final Map<ResourceLocation, Category> categories = Maps.newHashMap();
    private final Map<ResourceLocation, Note> notes = Maps.newHashMap();
    private final HolderLookup.Provider holderLookupProvider;

    public AbstractNoteProvider(PackOutput output, HolderLookup.Provider holderLookupProvider) {
        this.categoryPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, MagnoliaLib.CATEGORIES_FOLDER);
        this.notePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, MagnoliaLib.NOTES_FOLDER);
        this.holderLookupProvider = holderLookupProvider;
    }

    @Override
    public @NotNull String getName() {
        return "notes";
    }

    @Override
    public @NotNull CompletableFuture<?> run(final @NotNull CachedOutput output) {
        final List<CompletableFuture<?>> list = new ArrayList<>();
        buildNotes(categories, notes);
        categories.forEach((key, category) -> list.add(DataProvider.saveStable(output, holderLookupProvider,Category.CODEC, category, categoryPathProvider.json(key))));
        notes.forEach((key, note) ->
        {
            if (!categories.containsKey(note.getCategory()))
                throw new IllegalStateException("Note " + note.id() + " has an invalid category " + note.getCategory());
            list.add(DataProvider.saveStable(output,holderLookupProvider, Note.CODEC, note, this.notePathProvider.json(key)));
        });


        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildNotes(Map<ResourceLocation, Category> categories, Map<ResourceLocation, Note> notes);
}
