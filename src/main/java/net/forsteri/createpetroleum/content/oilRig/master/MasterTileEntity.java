package net.forsteri.createpetroleum.content.oilRig.master;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.forsteri.createpetroleum.content.oilRig.fluidOutput.FluidOutputTileEntity;
import net.forsteri.createpetroleum.content.oilRig.util.ISlaveTileEntity;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

@SuppressWarnings("deprecation")
public class MasterTileEntity extends SmartTileEntity implements IHaveGoggleInformation {

    public MasterTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

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

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putIntArray("slaveTileEntitiesX", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getX()).toArray());
        compound.putIntArray("slaveTileEntitiesY", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getY()).toArray());
        compound.putIntArray("slaveTileEntitiesZ", slaveTileEntities.stream().mapToInt(tileEntity -> tileEntity.getBlockPos().getZ()).toArray());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if(compound.contains("slaveTileEntitiesX") && compound.contains("slaveTileEntitiesY") && compound.contains("slaveTileEntitiesZ"))
            slaveTileEntitiesGetter = (level) ->
        {
            List<SmartTileEntity> ret = new ArrayList<>();
            for (int i = 0; i < compound.getIntArray("slaveTileEntitiesX").length; i++)
                ret.add(
                        (SmartTileEntity)
                                level.getBlockEntity(new BlockPos(
                                        compound.getIntArray("slaveTileEntitiesX")[i],
                                        compound.getIntArray("slaveTileEntitiesY")[i],
                                        compound.getIntArray("slaveTileEntitiesZ")[i])));
            return ret;
        };

        super.read(compound, clientPacket);
    }

    public Function<Level,List<SmartTileEntity>> slaveTileEntitiesGetter;

    @Override
    public void tick() {
        if(slaveTileEntitiesGetter != null && slaveTileEntities.isEmpty()) {
            slaveTileEntities = slaveTileEntitiesGetter.apply(level);
            slaveTileEntitiesGetter = null;
        }
        super.tick();
        if(canBeUsed())
            for(SmartTileEntity slaveTileEntity : slaveTileEntities)
                if(slaveTileEntity instanceof FluidOutputTileEntity)
                    ((FluidOutputTileEntity) slaveTileEntity).getInventory().fill(new FluidStack(Registration.CRUDE_OIL.get(), 10), IFluidHandler.FluidAction.EXECUTE);

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        boolean haveSpeed = false;

        if(!supportBiome())
            tooltip.add(componentSpacing.plainCopy()
                    .append(
                            Registration.BIOME_NOT_SUPPORTED));
        for (SmartTileEntity slaveTileEntity : slaveTileEntities) {
            if(slaveTileEntity instanceof KineticTileEntity){
                if(((KineticTileEntity) slaveTileEntity).getSpeed() == 0){
                    tooltip.add(Registration.NO_SPEED);
                    haveSpeed = true;
                    break;
                }
            }
        }

        return !supportBiome() || !haveSpeed;
    }

    public boolean canBeUsed(){
        return supportBiome() || haveSpeed();
    }

    public boolean supportBiome(){
        List<ResourceKey<Biome>> supportedBiomes = new ArrayList<>();
        supportedBiomes.add(Biomes.DESERT);
        supportedBiomes.add(Biomes.BADLANDS);
        supportedBiomes.add(Biomes.ERODED_BADLANDS);
        supportedBiomes.add(Biomes.WOODED_BADLANDS);
        supportedBiomes.add(Biomes.SNOWY_BEACH);
        supportedBiomes.add(Biomes.SNOWY_PLAINS);
        supportedBiomes.add(Biomes.SNOWY_TAIGA);
        supportedBiomes.add(Biomes.SNOWY_SLOPES);

        assert level != null;
        Holder<Biome> biome = level.getBiome(worldPosition);

        boolean isSupportedBiome = false;

        for (ResourceKey<Biome> supportedBiome : supportedBiomes
        ) {
            if(biome.is(supportedBiome)) {
                isSupportedBiome = true;
                break;
            }
        }

        return isSupportedBiome
        //        || true
                ;
    }

    public boolean haveSpeed(){
        boolean haveSpeed = false;

        for (SmartTileEntity slaveTileEntity : slaveTileEntities) {
            if(slaveTileEntity instanceof KineticTileEntity){
                if(((KineticTileEntity) slaveTileEntity).getSpeed() == 0){
                    haveSpeed = true;
                    break;
                }
            }
        }

        return haveSpeed;
    }
}
