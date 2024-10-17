package org.hiedacamellia.magnolialib.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;

public class SimplePenguinRecipe<R extends SimplePenguinRecipe<?>> extends AbstractSimplePenguinRecipe<R, SimplePenguinRecipe.Serializer<R>, ItemStack> {
    public SimplePenguinRecipe(DeferredHolder<RecipeType<?>, RecipeType<R>> recipeType, DeferredHolder<RecipeSerializer<?>, Serializer<R>> recipeSerializer, Ingredient ingredient, ItemStack output) {
        super(recipeType, recipeSerializer, ingredient, output);
    }

    public static class Serializer<T extends SimplePenguinRecipe<?>> implements RecipeSerializer<T> {
        public static <T extends SimplePenguinRecipe<?>> MapCodec<T> createCodec(Serializer.IRecipeFactory<T> factory) {
            return RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter((p_311729_) -> p_311729_.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter((p_311730_) -> p_311730_.output)
            ).apply(instance, factory::create));
        }

        public final StreamCodec<RegistryFriendlyByteBuf, T> createstreamCodec(Serializer.IRecipeFactory<T> factory) {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,(e)->e.ingredient,
                    ItemStack.STREAM_CODEC, (e)->e.output,
                    factory::create
            );
        }
        
        private final Serializer.IRecipeFactory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        public Serializer(Serializer.IRecipeFactory<T> factory) {
            this.factory = factory;
            this.codec = createCodec(factory);
            this.streamCodec = createstreamCodec(factory);
        }

        @Override
        public MapCodec<T> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return streamCodec;
        }

        public interface IRecipeFactory<T extends SimplePenguinRecipe<?>> {
            T create(Ingredient input, ItemStack output);
        }
    }
}
