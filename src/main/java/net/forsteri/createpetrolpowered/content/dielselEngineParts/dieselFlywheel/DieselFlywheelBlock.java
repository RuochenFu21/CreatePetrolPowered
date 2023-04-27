package net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine.DieselEngineBlock;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DieselFlywheelBlock extends RotatedPillarKineticBlock implements ITE<DieselFlywheelTileEntity> {

    public DieselFlywheelBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        Direction.Axis thisAxis = pState.getValue(AXIS);

        if (thisAxis.isHorizontal()){
            List<BlockPos> placeCheckRequired = new ArrayList<>();

            for (Direction direction : Direction.values())
                if (direction.getAxis() == thisAxis)
                    placeCheckRequired.add(pPos.relative(direction.getClockWise(Direction.Axis.Y)));

            for (BlockPos pos : placeCheckRequired)
                if (pLevel.getBlockState(pos).getBlock() instanceof DieselEngineBlock)
                    return false;
        }

        return true;
    }

    @Override
    public Class<DieselFlywheelTileEntity> getTileEntityClass() {
        return DieselFlywheelTileEntity.class;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return AllShapes.LARGE_GEAR.get(pState.getValue(AXIS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntityType<? extends DieselFlywheelTileEntity> getTileEntityType() {
        return Registration.DIESEL_FLYWHEEL_TILE.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public float getParticleTargetRadius() {
        return 2f;
    }

    @Override
    public float getParticleInitialRadius() {
        return 1.75f;
    }

}