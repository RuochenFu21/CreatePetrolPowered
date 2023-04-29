package net.forsteri.createpetrolpowered.content.oilRig.master;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class MasterBlock extends HorizontalDirectionalBlock implements IWrenchable, ITE<MasterTileEntity> {
    public MasterBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        worldIn.setBlock(pos.above(), Registration.KINETIC_INPUT_BLOCK.getDefaultState().setValue(FACING, state.getValue(FACING)), 3);
        worldIn.setBlock(pos.relative(state.getValue(FACING)), Registration.GHOST_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.relative(state.getValue(FACING).getOpposite()), Registration.GHOST_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.below(), Registration.GHOST_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.relative(state.getValue(FACING)).below(), Registration.GHOST_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.relative(state.getValue(FACING).getOpposite()).below(), Registration.FLUID_OUTPUT_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.relative(state.getValue(FACING)).above(), Registration.GHOST_BLOCK.getDefaultState(),3);
        worldIn.setBlock(pos.relative(state.getValue(FACING).getOpposite()).above(), Registration.GHOST_BLOCK.getDefaultState(),3);

        ((MasterTileEntity) Objects.requireNonNull(worldIn.getBlockEntity(pos))).initializeSlaves();
    }

    @Override
    public Class<MasterTileEntity> getTileEntityClass() {
        return MasterTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends MasterTileEntity> getTileEntityType() {
        return Registration.MASTER_TILE_ENTITY.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection()
                        .getOpposite());
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }
}
