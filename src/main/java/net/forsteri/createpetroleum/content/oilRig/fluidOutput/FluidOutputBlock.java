package net.forsteri.createpetroleum.content.oilRig.fluidOutput;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

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
}
