package net.forsteri.createpetroleum.content.oilRig.util;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.forsteri.createpetroleum.content.oilRig.master.MasterTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public abstract class AbstractSlaveTileEntity extends SmartTileEntity implements ISlaveTileEntity{
    public AbstractSlaveTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public MasterTileEntity getMasterTileEntity() {
        return masterTileEntity;
    }

    protected MasterTileEntity masterTileEntity;

    @Override
    public void setMasterTileEntity(MasterTileEntity masterTileEntity) {
        this.masterTileEntity = masterTileEntity;
    }

    @Override
    public void remove() {
        super.destroy();
        ISlaveTileEntity.super.onRemove();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        if(masterTileEntity != null){
            tag.putInt("masterX", masterTileEntity.getBlockPos().getX());
            tag.putInt("masterY", masterTileEntity.getBlockPos().getY());
            tag.putInt("masterZ", masterTileEntity.getBlockPos().getZ());
        }
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        if(tag.contains("masterX") && tag.contains("masterY") && tag.contains("masterZ"))
            masterTileEntity = (MasterTileEntity) Objects.requireNonNull(level).getBlockEntity(new BlockPos(tag.getInt("masterX"), tag.getInt("masterY"), tag.getInt("masterZ")));
        else {
            Objects.requireNonNull(level).destroyBlock(getBlockPos(), false);
        }
        super.read(tag, clientPacket);
    }
}
