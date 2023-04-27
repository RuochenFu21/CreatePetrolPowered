package net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselFlywheel;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine.DieselEngineBlock;
import net.forsteri.createpetrolpowered.content.dielselEngineParts.dieselEngine.DieselEngineTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DieselFlywheelTileEntity extends GeneratingKineticTileEntity {

    LerpedFloat visualSpeed = LerpedFloat.linear();
    public float angle;

    private int lastBurn;

    public List<DieselEngineTileEntity> engines = new ArrayList<>();

    public DieselFlywheelTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        assert level != null;

        lastBurn = burn();
        updateGeneratedRotation();

        if (!level.isClientSide)
            return;

        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 3 / 10f;
        angle %= 360;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket)
            visualSpeed.chase(getGeneratedSpeed(), 1 / 64f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public float getGeneratedSpeed() {
        return lastBurn == 0 ? 0 : 256;
    }

    protected int burn() {
        refreshEngines();

        int speedGenerates = 0;

        for(DieselEngineTileEntity engine: engines)
            speedGenerates += engine.generateSpeed();

        return speedGenerates;
    }

    protected void refreshEngines(){
        engines.clear();

        if (level == null)
            return;

        Function<Direction.Axis, Pair<BlockPos, BlockPos>> axisToPoses =
                axis -> switch (axis) {
                    case Z -> new Pair<>(
                            worldPosition.relative(Direction.EAST, 2),
                            worldPosition.relative(Direction.WEST, 2)
                    );
                    case Y -> new Pair<>(
                            worldPosition.relative(Direction.UP, 2),
                            worldPosition.relative(Direction.DOWN, 2)
                    );
                    case X -> new Pair<>(
                            worldPosition.relative(Direction.SOUTH, 2),
                            worldPosition.relative(Direction.NORTH, 2)
                    );
                };

        Pair<BlockPos, BlockPos> engine = axisToPoses.apply(getBlockState().getValue(DieselFlywheelBlock.AXIS));
        Pair<BlockState, BlockState> stateAtWheelPos = new Pair<>(level.getBlockState(engine.getFirst()),
                level.getBlockState(engine.getSecond()));

        for (Pair<BlockPos, BlockState> state: Arrays.asList(
                new Pair<>(
                        engine.getFirst(),
                        stateAtWheelPos.getFirst()
                ),
                new Pair<>(
                        engine.getSecond(),
                        stateAtWheelPos.getSecond()
                ))) {



            if (state.getSecond().getBlock() instanceof DieselEngineBlock &&
                    level.getBlockEntity(state.getFirst()) instanceof DieselEngineTileEntity) {
                ((DieselEngineTileEntity) Objects.requireNonNull(level.getBlockEntity(state.getFirst()))).refreshWheelState();
                if (((DieselEngineTileEntity) Objects.requireNonNull(level.getBlockEntity(state.getFirst()))).wheel == this)
                    this.engines.add((DieselEngineTileEntity) Objects.requireNonNull(level.getBlockEntity(state.getFirst())));
            }
        }
    }

    @Override
    public float calculateAddedStressCapacity() {
        return lastBurn;
    }
}
