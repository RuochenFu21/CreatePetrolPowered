package net.forsteri.createpetrolpowered.content;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
public class BurnableOilBucket extends BucketItem {
    public BurnableOilBucket(Fluid pContent, Properties pProperties) {
        super(pContent, pProperties);
    }

    public BurnableOilBucket(java.util.function.Supplier<? extends Fluid> pFluidSupplier, Properties pProperties) {
        super(pFluidSupplier, pProperties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 12800;
    }
}
