package net.forsteri.createpetroleum.content.oilRig.kineticInput;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.instancing.ConditionalInstance;
import com.jozufozu.flywheel.core.instancing.GroupInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.light.LightVolume;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import net.forsteri.createpetroleum.entry.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class KineticInputInstance extends SingleRotatingInstance implements DynamicInstance {
    protected ModelData rotated_part;

    protected float dir = 0;

    protected GroupInstance<OrientedData> hoseInstances;

    protected GridAlignedBB volume = new GridAlignedBB();

    protected KineticTileEntity tileEntity;

    protected ConditionalInstance<OrientedData> halfRope;

    protected float ticks = 0;
    public KineticInputInstance(MaterialManager modelManager, KineticInputTileEntity tile) {
        super(modelManager, tile);
        this.tileEntity = tile;

        Instancer<ModelData> model = materialManager.defaultSolid()
                .material(Materials.TRANSFORMED).getModel(Registration.ROTATING_PART);

        rotated_part = model.createInstance();

        rotated_part.loadIdentity()
                .translate(getInstancePosition())
                .setBlockLight(world.getBrightness(LightLayer.BLOCK, pos));
        rotated_part.rotateCentered(Direction.UP,
                switch (blockState.getValue(KineticInputBlock.HORIZONTAL_FACING)) {
                    case NORTH -> 0;
                    case EAST -> (float) (Math.PI / 2) * 3;
                    case SOUTH -> (float) Math.PI;
                    case WEST ->(float) (Math.PI / 2);
                    default -> throw new RuntimeException("Invalid direction");
                }
        );

        hoseInstances =
                new GroupInstance<>(materialManager.defaultCutout().material(Materials.ORIENTED).getModel(AllBlockPartials.HOSE, AllBlocks.HOSE_PULLEY.getDefaultState()));

        halfRope = new ConditionalInstance<>(
                getOrientedMaterial().getModel(AllBlockPartials.HOSE_HALF, AllBlocks.HOSE_PULLEY.getDefaultState()))
                .withCondition(() -> getRequiredRopeLength() % 1 < 0.75);

        LightVolume light = new LightVolume(world, volume);
        light.initialize();

    }

    @Override
    public void remove() {
        super.remove();
        rotated_part.delete();
        hoseInstances.clear();
        halfRope.delete();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(getWorldPosition(), rotated_part);
    }

    @Override
    protected BlockState getRenderedBlockState() {
        return shaft();
    }

    @Override
    public void beginFrame() {
        if (tileEntity.getSpeed() != 0) {

            ticks++;

            dir += ((float) Math.cos(ticks / 10)/20);

            rotated_part
                    .rotateCentered(Direction.EAST, ((float) Math.cos(ticks / 10)/20))
                    .setBlockLight(15)
                    .setSkyLight(15);
        }

        hoseInstances.resize(((int) Math.floor(
                getRequiredRopeLength()
        )));

        halfRope.update()
                .get()
                .ifPresent(rope1 -> rope1.setPosition(getInstancePosition().relative(blockState.getValue(KineticInputBlock.HORIZONTAL_FACING)))
                        .nudge(0, ((float) Math.sin(dir) - 0.45f), 0)
                        .setBlockLight(15)
                        .setSkyLight(15));

        if(halfRope.get().isEmpty())
            hoseInstances.get(1)
                .setPosition(getInstancePosition().relative(blockState.getValue(KineticInputBlock.HORIZONTAL_FACING)))
                .setBlockLight(15)
                .setSkyLight(15);

        for(int i = 0; i < hoseInstances.size(); i++){

            BlockPos pPos = getInstancePosition().relative(blockState.getValue(KineticInputBlock.HORIZONTAL_FACING)).below(i + 1);

            OrientedData instance = hoseInstances.get(i);
            instance
                    .setPosition(pPos)
                    .setBlockLight(15)
                    .setSkyLight(15);
        }
    }

    protected float getRequiredRopeLength() {
        float length = 3;

        BlockPos blockPos = pos.relative(blockState.getValue(KineticInputBlock.HORIZONTAL_FACING)).below().below().below();

        while (true){
            if(world.getBlockState(blockPos).isAir()){
                length++;
                blockPos = blockPos.below();
            }else{
                break;
            }
        }

        return length + ((float) Math.sin(dir));
    }

    @Override
    public @NotNull ImmutableBox getVolume() {
        return volume;
    }
}
