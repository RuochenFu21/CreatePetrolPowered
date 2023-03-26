package net.forsteri.createpetrolpowered.content;

import net.forsteri.createindustrialchemistry.substances.abstracts.FluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BitumenFluidBlock extends FluidBlock {
    public BitumenFluidBlock(Supplier<? extends FlowingFluid> pFluid, Properties pProperties) {
        super(pFluid, pProperties);
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
        pEntity.makeStuckInBlock(pState, new Vec3(0.25D, 0.05F, 0.25D));
    }
}
