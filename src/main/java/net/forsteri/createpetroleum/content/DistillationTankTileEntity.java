package net.forsteri.createpetroleum.content;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DistillationTankTileEntity extends FluidTankTileEntity {
    public DistillationTankTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return 1;
        return getMaxWidth();
    }
}

