package org.hiedacamellia.magnolialib.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.hiedacamellia.magnolialib.util.registry.ReloadableRegistry;

import javax.annotation.Nonnull;

public class MagnoliaRegistryItem<R extends ReloadableRegistry.PenguinRegistry<R> & MagnoliaRegistryItem.Nameable> extends Item {
    protected final ReloadableRegistry<R> registry;
    protected final String name;

    public MagnoliaRegistryItem(ReloadableRegistry<R> registry, String name, Properties properties) {
        super(properties);
        this.registry = registry;
        this.name = name;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public R fromStack(ItemStack stack) {
        return stack.hasTag() ? registry.get(new ResourceLocation(stack.getTag().getString(name))) : null;
    }

    public ItemStack toStack(R data) {
        return toStack(new ItemStack(this), data);
    }

    public ItemStack toStack(ResourceLocation id) {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putString(name, id.toString());
        return stack;
    }

    public ItemStack toStack(ItemStack stack, R data) {
        stack.getOrCreateTag().putString(name, data.id().toString());
        return stack;
    }

    @Nonnull
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        R data = fromStack(stack);
        return data != null ? data.name() : super.getName(stack);
    }

    public interface Nameable {
        Component name();
    }
}
