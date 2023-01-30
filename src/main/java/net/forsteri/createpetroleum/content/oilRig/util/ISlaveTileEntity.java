package net.forsteri.createpetroleum.content.oilRig.util;

import net.forsteri.createpetroleum.content.oilRig.master.MasterTileEntity;

import java.util.Objects;

public interface ISlaveTileEntity {


    MasterTileEntity getMasterTileEntity();

    void setMasterTileEntity(MasterTileEntity masterTileEntity);


    default void onRemove(){
        if(getMasterTileEntity() != null)
            Objects.requireNonNull(getMasterTileEntity().getLevel()).removeBlock(getMasterTileEntity().getBlockPos(), false);
    }
}
