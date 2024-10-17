package org.hiedacamellia.magnolialib.data.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.world.item.MagnoliaItems;

import java.util.concurrent.CompletableFuture;

public final class PenguinBannerTags extends BannerPatternTagsProvider {
    public PenguinBannerTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MagnoliaLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(MagnoliaItems.REQUIRES_PENGUIN_ITEM).add(MagnoliaItems.PENGUIN_PATTERN_KEY);
    }
}
