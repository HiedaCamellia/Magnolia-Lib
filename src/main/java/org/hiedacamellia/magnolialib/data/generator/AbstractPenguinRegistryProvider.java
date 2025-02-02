package org.hiedacamellia.magnolialib.data.generator;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPenguinRegistryProvider<T extends ReloadableRegistry.PenguinRegistry<T>> implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    private final Map<ResourceLocation, T> entries = Maps.newHashMap();
    private final ReloadableRegistry<T> registry;
    private final HolderLookup.Provider holderLookupProvider;

    public AbstractPenguinRegistryProvider(PackOutput output, ReloadableRegistry<T> registry,HolderLookup.Provider holderLookupProvider) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, registry.dir());
        this.registry = registry;
        this.holderLookupProvider = holderLookupProvider;
    }

    @Override
    public @NotNull String getName() {
        String[] split = registry.dir().split("/");
        if (split.length > 1) return WordUtils.capitalizeFully(split[1]);
        else return WordUtils.capitalizeFully(split[0]);
    }

    @Override
    public @NotNull CompletableFuture<?> run(final @NotNull CachedOutput output) {
        final List<CompletableFuture<?>> list = new ArrayList<>();
        buildRegistry(entries);
        entries.forEach((key, category) -> list.add(DataProvider.saveStable(output, holderLookupProvider,registry.codec(), category, pathProvider.json(key))));
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected abstract void buildRegistry(Map<ResourceLocation, T> map);
}
