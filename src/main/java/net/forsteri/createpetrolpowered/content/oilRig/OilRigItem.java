package net.forsteri.createpetrolpowered.content.oilRig;

import net.forsteri.createpetrolpowered.content.oilRig.master.MasterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OilRigItem extends BlockItem {
    public OilRigItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean canPlace(@NotNull BlockPlaceContext pContext, BlockState pState) {
        Direction facing = pState.getValue(MasterBlock.FACING);

        boolean canPlace = true;

        BlockPos midPos = getMidPos(pContext);

        if(midPos == null) return false;

        List<BlockPos> positions = new ArrayList<>();
        positions.add(midPos);
        positions.add(midPos.above());
        positions.add(midPos.below());
        positions.add(midPos.relative(facing));
        positions.add(midPos.relative(facing).below());
        positions.add(midPos.relative(facing).above());
        positions.add(midPos.relative(facing.getOpposite()));
        positions.add(midPos.relative(facing.getOpposite()).below());
        positions.add(midPos.relative(facing.getOpposite()).above());

        for(BlockPos pos : positions)
            if(!pContext.getLevel().getBlockState(pos).canBeReplaced(pContext))
                canPlace = false;

        return canPlace && super.canPlace(pContext, pState);
    }

    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext pContext, @NotNull BlockState pState) {

        BlockPos midPos = getMidPos(pContext);

        if(midPos == null) return false;

        return pContext.getLevel().setBlock(midPos, pState, 11);
    }

    @Nullable
    protected BlockPos getMidPos(BlockPlaceContext pContext){
        BlockPos midPos = pContext.getClickedPos();

        if(!pContext.getLevel().getBlockState(midPos.above()).canBeReplaced(pContext)
                &&  pContext.getLevel().getBlockState(midPos.below()).canBeReplaced(pContext))
            midPos = midPos.below();
        else if(!pContext.getLevel().getBlockState(midPos.below()).canBeReplaced(pContext)
                &&       pContext.getLevel().getBlockState(midPos.above()).canBeReplaced(pContext))
            midPos = midPos.above();
        else if(!pContext.getLevel().getBlockState(midPos.above()).canBeReplaced(pContext)
                && !pContext.getLevel().getBlockState(midPos.below()).canBeReplaced(pContext))
            return null;
        return midPos;
    }
}
