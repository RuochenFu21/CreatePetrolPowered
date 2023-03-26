package net.forsteri.createpetrolpowered.content;

import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.forsteri123.unlimitedfluidity.core.RisingGas;
import org.jetbrains.annotations.NotNull;

public abstract class NaturalGasFluid extends RisingGas {
    public NaturalGasFluid(ForgeFlowingFluid.Properties properties) {
        super(properties);
    }

    public static class Source extends NaturalGasFluid {
        public Source(ForgeFlowingFluid.Properties properties) {
            super(properties);
        }

        @Override
        public boolean isSource(@NotNull FluidState pState) {
            return true;
        }

        @Override
        public int getAmount(@NotNull FluidState pState) {
            return 8;
        }
    }

    public static class Flowing extends NaturalGasFluid {
        public Flowing(ForgeFlowingFluid.Properties properties) {
            super(properties);
        }

        @Override
        public boolean isSource(@NotNull FluidState pState) {
            return false;
        }

        @Override
        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }
    }
}
