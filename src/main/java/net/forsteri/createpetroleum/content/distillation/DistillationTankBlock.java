package net.forsteri.createpetroleum.content.distillation;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DistillationTankBlock extends FluidTankBlock{

    public DistillationTankBlock(Properties p_i48440_1_) {
        super(p_i48440_1_, false);
    }

    @Override
    public BlockEntityType<? extends FluidTankTileEntity> getTileEntityType() {
        return Registration.DISTILLATION_TANK_TILE_ENTITY.get();
    }
}