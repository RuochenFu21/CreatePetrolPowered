package net.forsteri.createpetroleum.content.oilRig.ghost;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GhostBlock extends Block implements IWrenchable, ITE<GhostTileEntity> {
    public GhostBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<GhostTileEntity> getTileEntityClass() {
        return GhostTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends GhostTileEntity> getTileEntityType() {
        return Registration.GHOST_TILE_ENTITY.get();
    }
}
