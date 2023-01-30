package net.forsteri.createpetroleum.content.oilRig.ghost;

import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.forsteri.createpetroleum.content.oilRig.util.AbstractSlaveTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GhostTileEntity extends AbstractSlaveTileEntity {
    public GhostTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
}
