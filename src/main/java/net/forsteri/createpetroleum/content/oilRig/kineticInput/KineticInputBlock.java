package net.forsteri.createpetroleum.content.oilRig.kineticInput;

import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class KineticInputBlock extends HorizontalKineticBlock implements IWrenchable, ITE<KineticInputTileEntity> {
    public KineticInputBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<KineticInputTileEntity> getTileEntityClass() {
        return KineticInputTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticInputTileEntity> getTileEntityType() {
        return Registration.KINETIC_INPUT_TILE_ENTITY.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    }
}
