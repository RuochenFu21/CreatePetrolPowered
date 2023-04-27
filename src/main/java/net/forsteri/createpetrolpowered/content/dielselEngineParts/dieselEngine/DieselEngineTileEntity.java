package net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.recipe.RecipeFinder;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel.DieselFlywheelBlock;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel.DieselFlywheelTileEntity;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.recipe.EngineRecipe;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class DieselEngineTileEntity extends SmartTileEntity {

    protected static final Object recipesKey = new Object();


    public DieselEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    public @Nullable DieselFlywheelTileEntity wheel = null;

    public void refreshWheelState(){
        if (level == null)
            return;

        BlockPos wheelPos = worldPosition.relative(getBlockState().getValue(DieselEngineBlock.FACING), 2);
        BlockState stateAtWheelPos = level.getBlockState(wheelPos);


        if (stateAtWheelPos.getBlock() instanceof DieselFlywheelBlock
                && stateAtWheelPos.getValue(DieselFlywheelBlock.AXIS).test(getBlockState().getValue(DieselEngineBlock.FACING).getClockWise(Direction.Axis.Y)))
            wheel = (DieselFlywheelTileEntity) level.getBlockEntity(wheelPos);
        else
            wheel = null;
    }

    protected boolean haveWheel() {
        refreshWheelState();
        return wheel != null;
    }

    protected EngineRecipe getUsingRecipe() {
        List<EngineRecipe> recipes =
                RecipeFinder.get(recipesKey, level, (recipe1 -> recipe1.getType() == Recipes.DIESEL_ENGINE_FUEL.getType()))
                        .stream().map(recipe -> (EngineRecipe) recipe)
                        .filter(distillationRecipe -> distillationRecipe.getFluidIngredients().get(0).test(getTank().getFluid(0)))
                        .toList();

        return recipes.size() > 0 ? recipes.get(0) : null;
    }

    public int generateSpeed() {
        assert level != null;
        if (level.isClientSide())
            return 0;

        EngineRecipe usingRecipe = getUsingRecipe();
        if (usingRecipe == null)
            return 0;

        int amount = usingRecipe.getFluidIngredients().get(0).getRequiredAmount();

        if (getTank().getFluid(0).getAmount() > amount) {
            getTank().getTankInventory().drain(amount, IFluidHandler.FluidAction.EXECUTE);
            return usingRecipe.getKineticEnergy();
        }

        getTank().getTankInventory().drain(getTank().getFluid(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
        return (int) (usingRecipe.getKineticEnergy() * (getTank().getFluid(0).getAmount() / ((float) amount)));
    }

    protected FluidTankTileEntity getTank() {
        if (level == null)
            return null;

        BlockEntity tank = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(DieselEngineBlock.FACING).getOpposite()));

        if (tank == null)
            return null;

        if (tank instanceof FluidTankTileEntity)
            return ((FluidTankTileEntity) tank).getControllerTE();

        return null;
    }
}

