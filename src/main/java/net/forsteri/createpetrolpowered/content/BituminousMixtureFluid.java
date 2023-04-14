package net.forsteri.createpetrolpowered.content;

import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class BituminousMixtureFluid extends ForgeFlowingFluid {
    @Override
    public Fluid getSource() {
        return Registration.BITUMINOUS_MIXTURE.get().getSource();
    }

    @Override
    protected void spread(LevelAccessor pLevel, @NotNull BlockPos pPos, @NotNull FluidState pState) {
        pLevel.setBlock(pPos, Registration.ASPHALT_CONCRETE.getDefaultState(), 3);
    }

    @Override
    public Fluid getFlowing() {
        return Registration.BITUMINOUS_MIXTURE.get().getFlowing();
    }

    protected BituminousMixtureFluid(Properties properties) {
        super(properties);
    }

    public static class Source extends BituminousMixtureFluid {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.@NotNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(net.minecraft.world.level.material.@NotNull FluidState state) {
            return true;
        }
    }
}
