package net.forsteri.createpetroleum.content.oilRig.fluidOutput;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.forsteri.createpetroleum.content.oilRig.util.AbstractSlaveTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidOutputTileEntity extends AbstractSlaveTileEntity {

    public LazyOptional<IFluidHandler> capability;

    protected IFluidHandler inventory = new SmartFluidTank(1000, (x) -> {});

    public FluidOutputTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        capability = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (isFluidHandlerCap(cap)) return capability.cast();
        return super.getCapability(cap, side);
    }

    public IFluidHandler getInventory() {
        return inventory;
    }
}
