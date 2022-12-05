package com.tynoxs.buildersdelight.compat.jei;

import com.tynoxs.buildersdelight.BuildersDelight;
import com.tynoxs.buildersdelight.content.recipe.ChiselRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    public static RecipeType TYPE = RecipeType.register("chisel");

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BuildersDelight.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        registration.addRecipeCategories(new ChiselRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        List<ChiselRecipe> recipeList = BuildersDelight.get().getRecipeFactory().getChiselRecipes();
        registration.addRecipes(mezz.jei.api.recipe.RecipeType.create(
                ChiselRecipeCategory.UID.getNamespace(),
                ChiselRecipeCategory.UID.getPath(),
                ChiselRecipe.class
        ), recipeList);
    }
}