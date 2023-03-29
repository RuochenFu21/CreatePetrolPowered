package net.forsteri.createpetrolpowered.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.utility.recipe.RecipeFinder;
import net.forsteri.createpetrolpowered.content.distillation.DistillationRecipe;
import net.forsteri.createpetrolpowered.content.distillation.DistillationTankTileEntity;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = FluidTankTileEntity.class, remap = false)
public abstract class MixinFluidTankTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IMultiTileContainer.Fluid {

    @Shadow public abstract boolean isController();

    @Shadow public abstract int getWidth();

    @Shadow public abstract FluidStack getFluid(int tank);

    @Shadow public abstract IFluidTank getTankInventory();

    @SuppressWarnings("unchecked")
    @Shadow public abstract FluidTankTileEntity getControllerTE();

    @Shadow public abstract int getHeight();

    public MixinFluidTankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if(this.isController()) {
            List<DistillationTankTileEntity> layers = getValidLayers();
            HeatCondition heatLevel = getHeatLevel();

            assert this.level != null;

            Map<HeatCondition, Integer> heatLevels = new HashMap<>();
            heatLevels.put(HeatCondition.NONE, 0);
            heatLevels.put(HeatCondition.HEATED, 1);
            heatLevels.put(HeatCondition.SUPERHEATED, 2);

            List<DistillationRecipe> recipes =
                    RecipeFinder.get(DistillationTankTileEntity.getRecipeCacheKey(), level, (recipe1 -> recipe1.getType() == Recipes.FRACTIONAL_DISTILLATION.getType()))
                            .stream().map(recipe -> (DistillationRecipe) recipe)
                            .filter(distillationRecipe -> heatLevels.get(heatLevel) >= heatLevels.get(distillationRecipe.getRequiredHeat()))
                            .filter(distillationRecipe -> distillationRecipe.getFluidIngredients().get(0).test(this.getFluid(0)))
                            .filter(distillationRecipe -> layers.size() >= distillationRecipe.getFluidResults().size())
                            .filter(
                                    distillationRecipe -> {
                                        for (int i = 0; i < distillationRecipe.getFluidResults().size(); i++) {
                                            if(
                                                    !distillationRecipe.getFluidResults().get(i).equals(layers.get(i).getFluid(0))
                                                 && !layers.get(i).getFluid(0).isEmpty()
                                            ){
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                            )

                            .toList()
            ;

            if(recipes.size() == 0)
                return;

            DistillationRecipe recipe = recipes.get(0);

            int speedMultiplier = getWidth()* getWidth() *
                    switch (heatLevel) {
                        case NONE -> 1;
                        case HEATED -> 2;
                        case SUPERHEATED -> 3;
                    };

            for (int i = 0; i < recipe.getFluidResults().size(); i++) {
                if(layers.get(i).getTankInventory().getFluidAmount() == layers.get(i).getTankInventory().getCapacity())
                    return;
            }


            if(this.getFluid(0).getAmount() >= recipe.getFluidIngredients().get(0).getRequiredAmount() * speedMultiplier) {
                for(int i = 0; i < recipe.getFluidResults().size(); i++)
                    layers.get(i).getTankInventory().fill(new FluidStack(recipe.getFluidResults().get(i).getFluid(), recipe.getFluidResults().get(i).getAmount() * speedMultiplier), IFluidHandler.FluidAction.EXECUTE);
                this.getTankInventory().drain(recipe.getFluidIngredients().get(0).getRequiredAmount() * speedMultiplier, IFluidHandler.FluidAction.EXECUTE);
            }else{
                if (this.getFluid(0).getAmount() != 0) {
                    for(int i = 0; i < recipe.getFluidResults().size(); i++)
                        layers.get(i).getTankInventory().fill(new FluidStack(recipe.getFluidResults().get(i).getFluid(),
                                recipe.getFluidIngredients().get(0).getRequiredAmount() / this.getFluid(0).getAmount() * recipe.getFluidResults().get(i).getAmount()
                        ), IFluidHandler.FluidAction.EXECUTE);
                    this.getTankInventory().drain(this.getFluid(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    protected boolean validLayer(Level world, BlockPos pos, int requiredWidth) {
        FluidTankTileEntity tankAt = ConnectivityHandler.partAt(
                Registration.DISTILLATION_TANK_TILE_ENTITY.get(), world, pos);

        if (tankAt == null) return false;

        FluidTankTileEntity controllerTE = tankAt.getControllerTE();

        if (controllerTE == null) return false;

        return controllerTE.getWidth() == requiredWidth;
    }

    protected List<DistillationTankTileEntity> getValidLayers(){
        List<DistillationTankTileEntity> layers = new ArrayList<>();
        if(validLayer(this.getLevel(), this.getBlockPos().above(), this.getWidth())) {
            layers.add(
                    (DistillationTankTileEntity) Objects.requireNonNull(this.getLevel()).getBlockEntity(this.getBlockPos().above()));
        }else {
            assert level != null;
            if(level.getBlockState(this.getBlockPos().above()).getBlock() != AllBlocks.FLUID_TANK.get()){
                return layers;
            }
        }
        int layerAt = 1;
        while(true) {
            ++layerAt;
            if(!validLayer(this.getLevel(), this.getBlockPos().above(layerAt), this.getWidth())) break;
            layers.add(
                    (DistillationTankTileEntity) Objects.requireNonNull(this.getLevel()).getBlockEntity(this.getBlockPos().above(layerAt)));
        }
        return layers;
    }

    protected HeatCondition getHeatLevel(){

        List<Integer> heatLevels = new ArrayList<>();

        for(BlockState burner : relatedBurners()){
            heatLevels.add(
                    switch (burner.getValue(BlazeBurnerBlock.HEAT_LEVEL)){
                        case SEETHING -> 2;
                        case NONE, SMOULDERING -> 0;
                        default -> 1;
                    }
            );
        }


        return switch (heatLevels.stream().min(Integer::compareTo).orElse(0)){
            case 0 -> HeatCondition.NONE;
            case 1 -> HeatCondition.HEATED;
            case 2 -> HeatCondition.SUPERHEATED;
            default -> throw new RuntimeException("Unexpected heat level");
        };
    }

    protected List<FluidTankTileEntity> relatedTanksInSameLayer(){
        List<FluidTankTileEntity> tanks = new ArrayList<>();
        tanks.add(this.getControllerTE());
        for(int i = 0; i < 6; ++i) {
            for(int j = 0; j < 6; ++j) {
                assert this.level != null;

                if(!(this.level.getBlockEntity(this.getBlockPos().offset(i - 2, 0, j - 2)) instanceof FluidTankTileEntity fluidTankTileEntity))
                    continue;

                if(this.getControllerTE() != null && fluidTankTileEntity.getControllerTE() == this.getControllerTE()) tanks.add(fluidTankTileEntity);
            }
        }
        return tanks;
    }

    protected List<BlockState> relatedBurners(){
        return
                relatedTanksInSameLayer().stream().map(
                        tank -> Objects.requireNonNull(tank.getLevel()).getBlockState(tank.getBlockPos().below())
                ).filter(blockState -> blockState.getBlock() instanceof BlazeBurnerBlock)
                        .toList()

                ;
    }
}
