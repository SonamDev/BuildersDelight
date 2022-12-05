package com.tynoxs.buildersdelight.content.block.connected.model;

import com.tynoxs.buildersdelight.content.block.connected.IConnectedTextureBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CTConnectedPaneBakedModel extends CTPaneBakedModel {

    private static final String UP_PROP_NAME = "up";
    private static final String DOWN_PROP_NAME = "down";
    private static final String UP_POST_PROP_NAME = "upPost";
    private static final String DOWN_POST_PROP_NAME = "downPost";
    private static final ModelProperty<GenericData<Map<Direction, Boolean>>> UP_PROP = new ModelProperty<>(GenericData.predicate(UP_PROP_NAME));
    private static final ModelProperty<GenericData<Map<Direction, Boolean>>> DOWN_PROP = new ModelProperty<>(GenericData.predicate(DOWN_PROP_NAME));
    private static final ModelProperty<GenericData<Boolean>> UP_POST_PROP = new ModelProperty<>(GenericData.predicate(UP_POST_PROP_NAME));
    private static final ModelProperty<GenericData<Boolean>> DOWN_POST_PROP = new ModelProperty<>(GenericData.predicate(DOWN_POST_PROP_NAME));


    public CTConnectedPaneBakedModel(IConnectedTextureBlock block){
        super(block);
    }


    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData){
        var modelData = ModelData.builder();
        modelData.with(SideData.IS_CT_BAKED_MODEL, SideData.CT_BAKED_MODEL_PREDICATE_DATA);
        var upMap = new HashMap<Direction, Boolean>();
        var downMap = new HashMap<Direction, Boolean>();
        for(Direction direction : Direction.Plane.HORIZONTAL){
            modelData.with(SideData.DIRECTION_MAPPING.get(direction), new SideData(direction, world, pos, state.getBlock(), true));
            BlockState upState = world.getBlockState(pos.above());
            boolean up = upState.getBlock() == state.getBlock() && upState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
            upMap.put(direction, up);
            modelData.with(UP_POST_PROP, new GenericData<Boolean>(UP_POST_PROP_NAME).withData(upState.getBlock() == state.getBlock()));
            BlockState downState = world.getBlockState(pos.below());
            boolean down = downState.getBlock() == state.getBlock() && downState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
            downMap.put(direction, down);
            modelData.with(DOWN_POST_PROP, new GenericData<Boolean>(DOWN_POST_PROP_NAME).withData(downState.getBlock() == state.getBlock()));
        }
        modelData.with(UP_PROP, new GenericData<Map<Direction, Boolean>>(UP_PROP_NAME).withData(upMap));
        modelData.with(DOWN_PROP, new GenericData<Map<Direction, Boolean>>(DOWN_PROP_NAME).withData(downMap));
        return modelData.build();
    }

    @Override
    protected boolean isEnabledUp(Direction part, ModelData extraData){
        return (part == null ? (extraData.has(UP_POST_PROP) ? extraData.get(UP_POST_PROP).getData() : false) :
                (extraData.has(UP_PROP) ? extraData.get(UP_PROP).getData().get(part) : false));
    }

    @Override
    protected boolean isEnabledDown(Direction part, ModelData extraData){
        return (part == null ? (extraData.has(UP_POST_PROP) ? extraData.get(DOWN_POST_PROP).getData() : false) :
                (extraData.has(DOWN_PROP) ? extraData.get(DOWN_PROP).getData().get(part) : false));    }

    @Override
    protected float[] getBorderUV(){
        return this.getUV(0, 7);
    }

    @Override
    protected float[] getUV(Direction side, ModelData modelData){
        if(side == Direction.UP || side == Direction.DOWN)
            return this.getBorderUV();

        if(!modelData.has(SideData.IS_CT_BAKED_MODEL))
            return getUV(0, 0);

        SideData blocks = modelData.get(SideData.DIRECTION_MAPPING.get(side));
        float[] uv;

        if(!blocks.left && !blocks.up && !blocks.right && !blocks.down) // all directions
            uv = this.getUV(0, 0);
        else{ // one direction
            if(blocks.left && !blocks.up && !blocks.right && !blocks.down)
                uv = this.getUV(3, 0);
            else if(!blocks.left && blocks.up && !blocks.right && !blocks.down)
                uv = this.getUV(0, 3);
            else if(!blocks.left && !blocks.up && blocks.right && !blocks.down)
                uv = this.getUV(1, 0);
            else if(!blocks.left && !blocks.up && !blocks.right && blocks.down)
                uv = this.getUV(0, 1);
            else{ // two directions
                if(blocks.left && !blocks.up && blocks.right && !blocks.down)
                    uv = this.getUV(2, 0);
                else if(!blocks.left && blocks.up && !blocks.right && blocks.down)
                    uv = this.getUV(0, 2);
                else if(blocks.left && blocks.up && !blocks.right && !blocks.down){
                    if(blocks.up_left)
                        uv = this.getUV(3, 3);
                    else
                        uv = this.getUV(5, 1);
                }else if(!blocks.left && blocks.up && blocks.right && !blocks.down){
                    if(blocks.up_right)
                        uv = this.getUV(1, 3);
                    else
                        uv = this.getUV(4, 1);
                }else if(!blocks.left && !blocks.up && blocks.right && blocks.down){
                    if(blocks.down_right)
                        uv = this.getUV(1, 1);
                    else
                        uv = this.getUV(4, 0);
                }else if(blocks.left && !blocks.up && !blocks.right && blocks.down){
                    if(blocks.down_left)
                        uv = this.getUV(3, 1);
                    else
                        uv = this.getUV(5, 0);
                }else{ // three directions
                    if(!blocks.left){
                        if(blocks.up_right && blocks.down_right)
                            uv = this.getUV(1, 2);
                        else if(blocks.up_right)
                            uv = this.getUV(4, 2);
                        else if(blocks.down_right)
                            uv = this.getUV(6, 2);
                        else
                            uv = this.getUV(6, 0);
                    }else if(!blocks.up){
                        if(blocks.down_left && blocks.down_right)
                            uv = this.getUV(2, 1);
                        else if(blocks.down_left)
                            uv = this.getUV(7, 2);
                        else if(blocks.down_right)
                            uv = this.getUV(5, 2);
                        else
                            uv = this.getUV(7, 0);
                    }else if(!blocks.right){
                        if(blocks.up_left && blocks.down_left)
                            uv = this.getUV(3, 2);
                        else if(blocks.up_left)
                            uv = this.getUV(7, 3);
                        else if(blocks.down_left)
                            uv = this.getUV(5, 3);
                        else
                            uv = this.getUV(7, 1);
                    }else if(!blocks.down){
                        if(blocks.up_left && blocks.up_right)
                            uv = this.getUV(2, 3);
                        else if(blocks.up_left)
                            uv = this.getUV(4, 3);
                        else if(blocks.up_right)
                            uv = this.getUV(6, 3);
                        else
                            uv = this.getUV(6, 1);
                    }else{ // four directions
                        if(blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                            uv = this.getUV(2, 2);
                        else{
                            if(!blocks.up_left && blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = this.getUV(7, 7);
                            else if(blocks.up_left && !blocks.up_right && blocks.down_left && blocks.down_right)
                                uv = this.getUV(6, 7);
                            else if(blocks.up_left && blocks.up_right && !blocks.down_left && blocks.down_right)
                                uv = this.getUV(7, 6);
                            else if(blocks.up_left && blocks.up_right && blocks.down_left && !blocks.down_right)
                                uv = this.getUV(6, 6);
                            else{
                                if(!blocks.up_left && blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = this.getUV(0, 4);
                                else if(blocks.up_left && !blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(0, 5);
                                else if(!blocks.up_left && !blocks.up_right && blocks.down_right && blocks.down_left)
                                    uv = this.getUV(3, 6);
                                else if(blocks.up_left && !blocks.up_right && !blocks.down_right && blocks.down_left)
                                    uv = this.getUV(3, 7);
                                else if(blocks.up_left && blocks.up_right && !blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(2, 7);
                                else if(!blocks.up_left && blocks.up_right && blocks.down_right && !blocks.down_left)
                                    uv = this.getUV(2, 6);
                                else{
                                    if(blocks.up_left)
                                        uv = this.getUV(5, 7);
                                    else if(blocks.up_right)
                                        uv = this.getUV(4, 7);
                                    else if(blocks.down_right)
                                        uv = this.getUV(4, 6);
                                    else if(blocks.down_left)
                                        uv = this.getUV(5, 6);
                                    else
                                        uv = this.getUV(0, 6);
                                }
                            }
                        }
                    }
                }
            }
        }

        return uv;
    }

    private float[] getUV(int x, int y){
        return new float[]{x * 2, y * 2, (x + 1) * 2, (y + 1) * 2};
    }

}
