package net.forsteri.createpetroleum.mixin;

import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FluidTankBlock.class, remap = false)
public abstract class MixinFluidTankBlock extends Block implements IWrenchable, ITE<FluidTankTileEntity> {
    protected MixinFluidTankBlock(Properties p_49795_) {
        super(p_49795_);
    }


}
