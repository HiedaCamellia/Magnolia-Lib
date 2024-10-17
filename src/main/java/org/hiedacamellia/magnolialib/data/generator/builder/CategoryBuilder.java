package org.hiedacamellia.magnolialib.data.generator.builder;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.tuple.Pair;
import org.hiedacamellia.magnolialib.util.icon.*;
import org.hiedacamellia.magnolialib.world.note.Category;
import org.hiedacamellia.magnolialib.world.note.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CategoryBuilder /*extends SimplePenguinBuilder<Category>*/ {
    private Icon icon = new ItemIcon(ItemStack.EMPTY);

    private final List<Pair<String, NoteBuilder>> notes = new ArrayList<>();

    public static CategoryBuilder category() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withItemIcon(ItemLike item) {
        icon = new ItemIcon(new ItemStack(item));
        return this;
    }

    public CategoryBuilder withTextureIcon(ResourceLocation texture, int x, int y) {
        icon = new TextureIcon(texture, x, y, 1);
        return this;
    }

    public CategoryBuilder withPenguinIcon(int x, int y) {
        icon = new TextureIcon(Icon.DEFAULT_LOCATION, x, y, 1);
        return this;
    }

    public CategoryBuilder withEntityIcon(EntityType<?> type, int scale) {
        icon = new EntityIcon(Holder.direct(type), 1, scale);
        return this;
    }

    public CategoryBuilder withNoteIcon() {
        icon = new TextureIcon(Icon.DEFAULT_LOCATION, 0, 0, 1);
        return this;
    }

    public CategoryBuilder withTagIcon(TagKey<Item> tag) {
        icon = new TagIcon(tag, 1);
        return this;
    }

    public CategoryBuilder withListIcon(Item... items) {
        icon = new ListIcon(Lists.newArrayList(items).stream().map(item -> new ItemIcon(new ItemStack(item))).collect(Collectors.toList()), 1);
        return this;
    }

    public NoteBuilder withNote(String name) {
        NoteBuilder builder = NoteBuilder.note(this);
        notes.add(Pair.of(name, builder));
        return builder;
    }

    public void save(Map<ResourceLocation, Category> categories, Map<ResourceLocation, Note> notes, ResourceLocation resource) {
        Category category = new Category();
        if (icon != null) category.setIcon(icon);
        categories.put(resource, category);
        this.notes.forEach(note -> notes.put(ResourceLocation.fromNamespaceAndPath(resource.getNamespace(), note.getKey()), note.getRight().withCategory(resource).toNote()));
    }
//
//    @Override
//    public void serializeRecipeData(@Nonnull JsonObject json) {
//        json.add("icon", icon.toJson(new JsonObject()));
//    }
}
