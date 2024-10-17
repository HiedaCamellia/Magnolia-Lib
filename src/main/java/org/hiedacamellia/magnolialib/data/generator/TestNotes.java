package org.hiedacamellia.magnolialib.data.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.hiedacamellia.magnolialib.world.note.Category;
import org.hiedacamellia.magnolialib.world.note.Note;

import java.util.Map;

public class TestNotes extends AbstractNoteProvider {
    public TestNotes(PackOutput output, HolderLookup.Provider holderLookupProvider) {
        super(output,holderLookupProvider);
    }

    @Override
    protected void buildNotes(Map<ResourceLocation, Category> categories, Map<ResourceLocation, Note> notes) {
//        CategoryBuilder.category().withItemIcon(Items.BAMBOO_CHEST_RAFT)
//                .withNote("energy").withNoteIcon().end()
//                .withNote("fishing").withItemIcon(Items.FISHING_ROD).setNoteType("lifespan").end() //TODO: With the piscary fishing rod
//                .withNote("shovel").withItemIcon(Items.STONE_SHOVEL).end() //TODO: With the HF shovel
//                .withNote("hammer").withItemIcon(Items.HANGING_ROOTS).end()
//                .withNote("axe").withItemIcon(Items.BLACK_SHULKER_BOX).end()
//                .save(categories, notes, new ResourceLocation(PenguinLib.MODID, "activities"));
    }
}
