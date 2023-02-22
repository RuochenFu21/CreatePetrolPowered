package net.forsteri.createpetroleum.content.oilRig.util;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.forsteri.createpetroleum.content.oilRig.master.MasterTileEntity;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;

public interface ISlaveTileEntity extends IHaveGoggleInformation {


    MasterTileEntity getMasterTileEntity();

    void setMasterTileEntity(MasterTileEntity masterTileEntity);


    default void onRemove(){
        if(getMasterTileEntity() != null)
            Objects.requireNonNull(getMasterTileEntity().getLevel()).removeBlock(getMasterTileEntity().getBlockPos(), false);
    }

    @Override
    default boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return getMasterTileEntity() != null && getMasterTileEntity().addToGoggleTooltip(tooltip, isPlayerSneaking);
    }
}
