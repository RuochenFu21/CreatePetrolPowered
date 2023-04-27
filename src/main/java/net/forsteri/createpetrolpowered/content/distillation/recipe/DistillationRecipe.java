package net.forsteri.createpetrolpowered.content.distillation.recipe;

import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DistillationRecipe extends ProcessingRecipe<SmartInventory> {

    public DistillationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(Recipes.FRACTIONAL_DISTILLATION, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean matches(@NotNull SmartInventory pContainer, @NotNull Level pLevel) {
        return false;
    }

    @Override
    public @NotNull HeatCondition getRequiredHeat() {
        return super.getRequiredHeat();
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }
}
