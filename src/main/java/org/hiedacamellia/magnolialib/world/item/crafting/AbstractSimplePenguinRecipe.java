package org.hiedacamellia.magnolialib.world.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;

@SuppressWarnings("NullableProblems")
public class AbstractSimplePenguinRecipe<RT extends Recipe<?>, RS extends RecipeSerializer<?>, O> implements Recipe<RecipeInput> {
    public final Ingredient ingredient;
    public final O output;
    protected final DeferredHolder<RecipeType<?>, RecipeType<RT>> type;
    protected final DeferredHolder<RecipeSerializer<?>, RS> serializer;

    public AbstractSimplePenguinRecipe(DeferredHolder<RecipeType<?>, RecipeType<RT>> recipeType, DeferredHolder<RecipeSerializer<?>, RS> recipeSerializer, Ingredient ingredient, O output) {
        this.type = recipeType;
        this.serializer = recipeSerializer;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public ItemStack getResult() {
        return ItemStack.EMPTY;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }
}
