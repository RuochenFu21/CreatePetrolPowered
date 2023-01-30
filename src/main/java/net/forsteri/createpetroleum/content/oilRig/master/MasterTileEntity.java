package net.forsteri.createpetroleum.content.oilRig.master;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.forsteri.createpetroleum.content.oilRig.util.ISlaveTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class MasterTileEntity extends KineticTileEntity {

    public MasterTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    List<SmartTileEntity> slaveTileEntities = new ArrayList<>();

    protected void initializeSlaves(){
        BlockState state = getBlockState();

        List<BlockPos> SlavePosList = new ArrayList<>();
        SlavePosList.add(worldPosition.above());
        SlavePosList.add(worldPosition.relative(state.getValue(FACING)));
        SlavePosList.add(worldPosition.relative(state.getValue(FACING).getOpposite()));
        SlavePosList.add(worldPosition.below());
        SlavePosList.add(worldPosition.relative(state.getValue(FACING)).below());
        SlavePosList.add(worldPosition.relative(state.getValue(FACING).getOpposite()).below());
        SlavePosList.add(worldPosition.relative(state.getValue(FACING)).above());
        SlavePosList.add(worldPosition.relative(state.getValue(FACING).getOpposite()).above());

        for (BlockPos slavePos : SlavePosList) {
            if (level != null) {
                ((ISlaveTileEntity) Objects.requireNonNull(level.getBlockEntity(slavePos))).setMasterTileEntity(this);
            }
            assert level != null;
            slaveTileEntities.add((SmartTileEntity) level.getBlockEntity(slavePos));
        }
    }

    @Override
    public void remove() {
        super.remove();
        for(SmartTileEntity slaveTileEntity : slaveTileEntities){
            Objects.requireNonNull(slaveTileEntity.getLevel()).removeBlock(slaveTileEntity.getBlockPos(), false);
        }
    }

//    @Override
//    protected void write(CompoundTag compound, boolean clientPacket) {
//        compound.putIntArray("slaveTileEntitiesX", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getX()).toArray());
//        compound.putIntArray("slaveTileEntitiesY", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getY()).toArray());
//        compound.putIntArray("slaveTileEntitiesZ", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getZ()).toArray());
//        super.write(compound, clientPacket);
//    }
//
//    @Override
//    protected void read(CompoundTag compound, boolean clientPacket) {
//        if(compound.contains("slaveTileEntitiesX") && compound.contains("slaveTileEntitiesY") && compound.contains("slaveTileEntitiesZ"))
//            for (int i = 0; i < compound.getIntArray("slaveTileEntitiesX").length; i++)
//                slaveTileEntities.add(
//                        (SmartTileEntity)
//                                level.getBlockEntity(new BlockPos(
//                                        compound.getIntArray("slaveTileEntitiesX")[i],
//                                        compound.getIntArray("slaveTileEntitiesY")[i],
//                                        compound.getIntArray("slaveTileEntitiesZ")[i])));
//
//        super.read(compound, clientPacket);
//    }
}
