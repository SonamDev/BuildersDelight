package com.tynoxs.buildersdelight.content.block.connected.model;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.HashMap;
import java.util.Map;

public class SideData {

    public static final String CT_BAKED_MODEL_PREDICATE_DATA = "CT_BAKED_MODEL";
    public static final Map<Direction, ModelProperty<SideData>> DIRECTION_MAPPING = new HashMap<>();
    public static final ModelProperty<String> IS_CT_BAKED_MODEL = new ModelProperty<>(data -> data.equals(CT_BAKED_MODEL_PREDICATE_DATA));
    public static final ModelProperty<SideData> DOWN_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.DOWN);
    public static final ModelProperty<SideData> UP_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.UP);
    public static final ModelProperty<SideData> NORTH_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.NORTH);
    public static final ModelProperty<SideData> SOUTH_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.SOUTH);
    public static final ModelProperty<SideData> WEST_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.WEST);
    public static final ModelProperty<SideData> EAST_PROP = new ModelProperty<>(data -> data.getDirection() == Direction.EAST);

    public static void initSideData() {
        DIRECTION_MAPPING.put(Direction.DOWN, SideData.DOWN_PROP);
        DIRECTION_MAPPING.put(Direction.UP, SideData.UP_PROP);
        DIRECTION_MAPPING.put(Direction.NORTH, SideData.NORTH_PROP);
        DIRECTION_MAPPING.put(Direction.SOUTH, SideData.SOUTH_PROP);
        DIRECTION_MAPPING.put(Direction.WEST, SideData.WEST_PROP);
        DIRECTION_MAPPING.put(Direction.EAST, SideData.EAST_PROP);
    }

    private Direction direction;
    private BlockGetter world;
    private Block block;

    public boolean left;
    public boolean right;
    public boolean up;
    public boolean up_left;
    public boolean up_right;
    public boolean down;
    public boolean down_left;
    public boolean down_right;

    public SideData(Direction side, BlockGetter world, BlockPos pos, Block block, boolean pane){
        this.world = world;
        this.block = block;

        this.direction = side;

        Direction left;
        Direction right;
        Direction up;
        Direction down;
        if(side.getAxis() == Direction.Axis.Y){
            if (pane) {
                left = side == Direction.UP ? Direction.WEST : Direction.EAST;
                right = side == Direction.UP ? Direction.EAST : Direction.WEST;
                up = Direction.NORTH;
                down = Direction.SOUTH;
            } else {
                left = Direction.WEST;
                right = Direction.EAST;
                up = side == Direction.UP ? Direction.NORTH : Direction.SOUTH;
                down = side == Direction.UP ? Direction.SOUTH : Direction.NORTH;
            }
        }else{
            left = side.getClockWise();
            right = side.getCounterClockWise();
            up = Direction.UP;
            down = Direction.DOWN;
        }

        this.left = this.isSameBlock(pos.relative(left));
        this.right = this.isSameBlock(pos.relative(right));
        this.up = this.isSameBlock(pos.relative(up));
        this.up_left = this.isSameBlock(pos.relative(up).relative(left));
        this.up_right = this.isSameBlock(pos.relative(up).relative(right));
        this.down = this.isSameBlock(pos.relative(down));
        this.down_left = this.isSameBlock(pos.relative(down).relative(left));
        this.down_right = this.isSameBlock(pos.relative(down).relative(right));
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isSameBlock(BlockPos pos){
        return this.world.getBlockState(pos).getBlock() == this.block;
    }

}
