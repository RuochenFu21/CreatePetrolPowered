package net.forsteri.createpetrolpowered.content.dielselEngineParts.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EngineRecipe extends ProcessingRecipe<SmartInventory> {
    protected int kineticEnergy;

    public EngineRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(Recipes.DIESEL_ENGINE_FUEL, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    public int getKineticEnergy() {
        return kineticEnergy;
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
        return 0;
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
        return false;
    }

    @Override
    public void readAdditional(@NotNull JsonObject json) {
        super.readAdditional(json);
        kineticEnergy = GsonHelper.getAsInt(json, "kineticEnergy");
    }

    @Override
    public void writeAdditional(@NotNull JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("kineticEnergy", kineticEnergy);
    }

    @Override
    public void readAdditional(@NotNull FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        kineticEnergy = buffer.readInt();
    }

    @Override
    public void writeAdditional(@NotNull FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeInt(kineticEnergy);
    }
}
