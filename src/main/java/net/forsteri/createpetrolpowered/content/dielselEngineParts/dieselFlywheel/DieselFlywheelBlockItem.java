package net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel;

import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine.DieselEngineBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DieselFlywheelBlockItem extends BlockItem {
    public DieselFlywheelBlockItem(DieselFlywheelBlock pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public @NotNull InteractionResult place(BlockPlaceContext pContext) {

        if (pContext.canPlace() && pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite())).getBlock() instanceof DieselEngineBlock && pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite())).getValue(DieselEngineBlock.FACING) == pContext.getClickedFace()) {
            if (pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace())).getBlock() instanceof AirBlock) {

                Direction engineFacing = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite())).getValue(DieselEngineBlock.FACING);

                BlockPlaceContext context = BlockPlaceContext.at(
                        pContext, pContext.getClickedPos().relative(pContext.getClickedFace()), engineFacing
                );

                return super.place(context);
            } else
                return InteractionResult.PASS;
        }

        return super.place(pContext);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        if (pContext.canPlace() && pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite()).relative(pContext.getClickedFace().getOpposite())).getBlock() instanceof DieselEngineBlock && pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite()).relative(pContext.getClickedFace().getOpposite())).getValue(DieselEngineBlock.FACING) == pContext.getClickedFace()) {
            if (super.getPlacementState(pContext) != null && Objects.requireNonNull(super.getPlacementState(pContext)).getBlock() instanceof DieselFlywheelBlock) {
                return Objects.requireNonNull(super.getPlacementState(pContext)).setValue(DieselFlywheelBlock.AXIS,
                        switch (pContext.getClickedFace().getAxis()) {
                            case X -> Direction.Axis.Z;
                            case Z -> Direction.Axis.X;
                            default -> throw new RuntimeException("Invalid axis");
                        }
                );
            }
        }

        return super.getPlacementState(pContext);
    }
}
