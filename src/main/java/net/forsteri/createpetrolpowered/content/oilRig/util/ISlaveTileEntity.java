package net.forsteri.createpetrolpowered.content.oilRig.util;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.forsteri.createpetrolpowered.content.oilRig.master.MasterTileEntity;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface ISlaveTileEntity extends IHaveGoggleInformation {


    MasterTileEntity getMasterTileEntity();

    void setMasterTileEntity(MasterTileEntity masterTileEntity);


    default void onRemove(){
        if (getMasterTileEntity() == null)
            return;

        if (getMasterTileEntity().getLevel() == null)
            return;

        getMasterTileEntity().getLevel().removeBlock(getMasterTileEntity().getBlockPos(), false);
//
//            var item = new ItemEntity(
//                    getMasterTileEntity().getLevel(),
//                    getMasterTileEntity().getBlockPos().getX(),
//                    getMasterTileEntity().getBlockPos().getY(),
//                    getMasterTileEntity().getBlockPos().getZ(),
//                    Registration.MASTER_BLOCK.asStack()
//            );
//
//            getMasterTileEntity().getLevel().addFreshEntity(item);
    }

    @Override
    default boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return getMasterTileEntity() != null && getMasterTileEntity().addToGoggleTooltip(tooltip, isPlayerSneaking);
    }
}
