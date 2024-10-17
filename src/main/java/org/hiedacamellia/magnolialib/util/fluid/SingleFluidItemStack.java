package org.hiedacamellia.magnolialib.util.fluid;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.function.Supplier;

public class SingleFluidItemStack extends FluidHandlerItemStack {
    private final Fluid fluid;

    public SingleFluidItemStack(Supplier<DataComponentType<SimpleFluidContent>> componentType, ItemStack container, Fluid fluid, int capacity) {
        super(componentType,container, capacity);
        this.fluid = fluid;
    }

    public SingleFluidItemStack(Fluid fluid, int capacity) {
        super(null,ItemStack.EMPTY, capacity);
        this.fluid = fluid;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid().equals(this.fluid);
    }
}