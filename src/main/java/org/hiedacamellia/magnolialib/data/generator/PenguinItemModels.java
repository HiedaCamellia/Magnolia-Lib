package org.hiedacamellia.magnolialib.data.generator;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.world.item.MagnoliaItems;

public class PenguinItemModels extends ItemModelProvider {
    public PenguinItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MagnoliaLib.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        MagnoliaItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .forEach(item -> {
                    String path = BuiltInRegistries.ITEM.getKey(item).getPath();
                    singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path.replace("_treat", "")));
                });
    }
}
