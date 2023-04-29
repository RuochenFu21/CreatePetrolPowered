package net.forsteri.createpetrolpowered.content.oilRig.fluidOutput;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FluidOutputBlock extends Block implements IWrenchable, ITE<FluidOutputTileEntity> {
    public FluidOutputBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<FluidOutputTileEntity> getTileEntityClass() {
        return FluidOutputTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidOutputTileEntity> getTileEntityType() {
        return Registration.FLUID_OUTPUT_TILE_ENTITY.get();
    }

    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.INVISIBLE;
    }
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
        return true;
    }

}
