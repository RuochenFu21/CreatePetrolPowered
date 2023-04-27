package net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel.DieselFlywheelBlock;
import net.minecraft.core.Direction;

public class DieselEngineInstance extends BlockEntityInstance<DieselEngineTileEntity> implements DynamicInstance {

    protected final ModelData shaft;
    protected final ModelData shaft2;

    public DieselEngineInstance(MaterialManager modelManager, DieselEngineTileEntity tile) {
        super(modelManager, tile);
        shaft = getTransformMaterial().getModel(AllBlocks.SHAFT.getDefaultState())
                .createInstance();

        shaft2 = getTransformMaterial().getModel(AllBlocks.SHAFT.getDefaultState())
                .createInstance();
    }

    @Override
    public void beginFrame() {
        if (blockEntity.haveWheel()) {
            placeShaft(shaft, 0.5);
            placeShaft(shaft2, 1.5);
        } else {
            shaft.scale(0, 0, 0);
            shaft2.scale(0, 0, 0);
        }
    }

    private void placeShaft(ModelData shaft, double distance) {
        shaft.scale(1, 1, 1);

        assert blockEntity.wheel != null;

        var angle = blockEntity.wheel.angle + 90;

        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);

        msr.translate(getInstancePosition().offset(blockEntity.wheel.getBlockPos().subtract(blockEntity.getBlockPos())));

        msr.translate(shaftAdjustment(blockEntity.getBlockState().getValue(DieselEngineBlock.FACING), Math.toRadians(angle)));

        msr.translate(
                blockEntity.getBlockState().getValue(DieselEngineBlock.FACING).getClockWise().getStepX() * 0.25,
                blockEntity.getBlockState().getValue(DieselEngineBlock.FACING).getClockWise().getStepY() * 0.25,
                blockEntity.getBlockState().getValue(DieselEngineBlock.FACING).getClockWise().getStepZ() * 0.25
        );

        double shaftDirection = (
                directions(Math.toRadians(angle) + Math.PI/2).getFirst() - Math.PI/2);

        switch (blockEntity.getBlockState().getValue(DieselEngineBlock.FACING)) {
            case NORTH ->
                    msr.translate(
                            0,
                            Math.cos(shaftDirection) * -distance,
                            Math.sin(shaftDirection) * -distance
                    );
            case SOUTH ->
                    msr.translate(
                            0,
                            Math.cos(shaftDirection) * distance,
                            Math.sin(shaftDirection) * distance
                    );
            case EAST ->
                    msr.translate(
                            Math.sin(shaftDirection) * distance,
                            Math.cos(shaftDirection) * -distance,
                            0
                    );
            case WEST ->
                    msr.translate(
                            Math.sin(shaftDirection) * -distance,
                            Math.cos(shaftDirection) * distance,
                            0
                    );
        }

        msr.rotateCentered(
                switch (blockEntity.wheel.getBlockState().getValue(DieselFlywheelBlock.AXIS)) {
                    case X -> Direction.WEST;
                    case Y -> Direction.UP;
                    case Z -> Direction.SOUTH;
                },
                (float) (blockEntity.wheel.getBlockState().getValue(DieselFlywheelBlock.AXIS) == Direction.Axis.X ?
                        -shaftDirection :
                        shaftDirection)
        );

        shaft.setTransform(ms);
    }

    private static Vector3f shaftAdjustment(Direction facing, double wheelDirectionRadian) {
        float x = 0;
        float y = 0;
        float z = 0;

        switch (facing) {
            case NORTH -> {
                z = (float) -Math.sin(wheelDirectionRadian);
                y = (float) -Math.cos(wheelDirectionRadian);
            }
            case SOUTH -> {
                z = (float) Math.sin(wheelDirectionRadian);
                y = (float) Math.cos(wheelDirectionRadian);
            }
            case EAST -> {
                x = (float) -Math.sin(-wheelDirectionRadian);
                y = (float) -Math.cos(-wheelDirectionRadian);
            }
            case WEST -> {
                x = (float) Math.sin(-wheelDirectionRadian);
                y = (float) Math.cos(-wheelDirectionRadian);
            }
            default -> {}
        }

        x = 13/32f * x;
        y = 13/32f * y;
        z = 13/32f * z;

        return new Vector3f(x, y, z);
    }

    private static Pair<Double, Double> directions(double directionRadian) {
        double distance = 4;

        var sinDir = Math.sin(directionRadian);
        var cosDir = Math.cos(directionRadian);

        return Pair.of(
                Math.atan(
                        sinDir / (cosDir - distance)
                ),
                Math.atan(
                        sinDir / (cosDir + distance)
                )
        );
    }

    @Override
    public void updateLight() {
        relight(pos, shaft, shaft2);
    }

    @Override
    protected void remove() {
        shaft.delete();
        shaft2.delete();
    }
}
