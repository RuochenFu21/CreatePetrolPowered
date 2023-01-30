package net.forsteri.createpetroleum.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.forsteri.createindustrialchemistry.entry.registers.substances.GasSubstances;
import net.forsteri.createindustrialchemistry.entry.registers.substances.LiquidSubstances;
import net.forsteri.createpetroleum.content.distillation.DistillationTankTileEntity;
import net.forsteri.createpetroleum.entry.Registration;
import net.forsteri.createpetroleum.util.HeatLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            HeatLevels heatLevel = getHeatLevel();

            int baseSpeed = 10;
            int speed = baseSpeed * getWidth()* getWidth() * heatLevel.getLevel();

            if(this.getFluid(0).getFluid() instanceof WaterFluid){
                if (layers.size() >= 2 && heatLevel.getLevel() >= 2) {
                    if(this.getFluid(0).getAmount() >= speed) {
                        this.getTankInventory().drain(speed, IFluidHandler.FluidAction.EXECUTE);
                        layers.get(1).getTankInventory().fill(new FluidStack(GasSubstances.WATER_VAPOR.SOURCE.get(), speed), IFluidHandler.FluidAction.EXECUTE);
                        layers.get(0).getTankInventory().fill(new FluidStack(LiquidSubstances.MOLTEN_SALT.SOURCE.get(), speed), IFluidHandler.FluidAction.EXECUTE);
                    }else{
                        this.getTankInventory().drain(this.getFluid(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                        layers.get(1).getTankInventory().fill(new FluidStack(GasSubstances.WATER_VAPOR.SOURCE.get(), this.getFluid(0).getAmount()), IFluidHandler.FluidAction.EXECUTE);
                        layers.get(0).getTankInventory().fill(new FluidStack(LiquidSubstances.MOLTEN_SALT.SOURCE.get(), this.getFluid(0).getAmount()), IFluidHandler.FluidAction.EXECUTE);
                    }
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

    protected HeatLevels getHeatLevel(){

        List<Integer> integers = new ArrayList<>();
        for(BlockState state: relatedBurners()) {
            if (state.getBlock() == Blocks.FIRE) integers.add(1);
            else if (state.getBlock() == AllBlocks.BLAZE_BURNER.get()) {
                if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.NONE)
                    integers.add(0);
                if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.SMOULDERING)
                    integers.add(1);
                if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.FADING)
                    integers.add(2);
                if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.KINDLED)
                    integers.add(2);
                if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.SEETHING)
                    integers.add(3);
            }else integers.add(0);
        }

        return HeatLevels.fromInt(Math.min(integers.stream().mapToInt(Integer::intValue).sum(), 3));
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
        return relatedTanksInSameLayer().stream().map((tank -> Objects.requireNonNull(tank.getLevel()).getBlockState(tank.getBlockPos().below()))).toList();
    }
}
