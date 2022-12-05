package com.tynoxs.buildersdelight.compat.jei;

import com.tynoxs.buildersdelight.BuildersDelight;
import com.tynoxs.buildersdelight.content.init.BdItems;
import com.tynoxs.buildersdelight.content.recipe.ChiselRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.guieffect.qual.UI;

import java.util.Arrays;

public class ChiselRecipeCategory implements IRecipeCategory<ChiselRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(BuildersDelight.MODID, "chisel");
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final Component localizedName;

    public ChiselRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("buildersdelight:textures/gui/chisel_gui_jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 176, 85);
        localizedName = Component.translatable("container.iron_chisel");
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BdItems.IRON_CHISEL.get()));
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }




//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChiselRecipe recipe, IFocusGroup focuses) {
//        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 8)
                .addItemStacks(recipe.getVariants());


        int index =0;
        for(int i = 0; i < 4; ++i) {
            for (int j = 0; j < 7; ++j) {
                if(index < recipe.getVariants().size()) {
                    ItemStack stack = recipe.getVariants().get(index);
                    builder.addSlot(RecipeIngredientRole.OUTPUT, 40 + j * 18, 8 + i * 18)
                            .addItemStacks(Arrays.asList(stack));
                    index += 1;
                }
            }
        }
    }

    @Override
    public RecipeType<ChiselRecipe> getRecipeType() {
        return RecipeType.create(UID.getNamespace(), UID.getPath(), ChiselRecipe.class);
    }

}