package net.forsteri.createpetrolpowered.content.oilRig.master;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.forsteri.createpetrolpowered.content.oilRig.fluidOutput.FluidOutputTileEntity;
import net.forsteri.createpetrolpowered.content.oilRig.util.ISlaveTileEntity;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (level == null)
            return;

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
            if (level.getBlockEntity(slavePos) == null) {
                return;
            }
        }

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

        assert level != null;
        var item = new ItemEntity(
                level,
                worldPosition.getX(),
                worldPosition.getY(),
                worldPosition.getZ(),
                Registration.MASTER_BLOCK.asStack()
        );

        level.addFreshEntity(item);

        for(SmartTileEntity slaveTileEntity : slaveTileEntities){
            Objects.requireNonNull(slaveTileEntity.getLevel()).removeBlock(slaveTileEntity.getBlockPos(), false);
        }
    }
    @Override
    public void tick() {
        super.tick();
        if (slaveTileEntities.isEmpty())
            initializeSlaves();

        if(canBeUsed())
            for(SmartTileEntity slaveTileEntity : slaveTileEntities)
                if(slaveTileEntity instanceof FluidOutputTileEntity)
                    ((FluidOutputTileEntity) slaveTileEntity).getInventory().fill(new FluidStack(Registration.CRUDE_OIL.get().getSource(), 10), IFluidHandler.FluidAction.EXECUTE);

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        if(!supportBiome())
            tooltip.add(componentSpacing.plainCopy()
                    .append(
                            Registration.BIOME_NOT_SUPPORTED));
        if(!haveSpeed())
            tooltip.add(componentSpacing.plainCopy()
                    .append(
                            Registration.NO_SPEED));

        return !supportBiome() || !haveSpeed();
    }

    public boolean canBeUsed(){
        return supportBiome() && haveSpeed();
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
                if(((KineticTileEntity) slaveTileEntity).getSpeed() != 0){
                    haveSpeed = true;
                    break;
                }
            }
        }

        return haveSpeed;
    }
}
