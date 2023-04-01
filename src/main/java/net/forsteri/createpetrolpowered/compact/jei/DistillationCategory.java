package net.forsteri.createpetrolpowered.compact.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.forsteri.createpetrolpowered.content.distillation.DistillationRecipe;
import net.forsteri.createpetrolpowered.entry.GuiTextures;
import org.jetbrains.annotations.NotNull;

public class DistillationCategory extends CreateRecipeCategory<DistillationRecipe> {
    public DistillationCategory(Info<DistillationRecipe> info) {
        super(info);
    }

    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    protected int getScale(DistillationRecipe recipe) {
        int scale = 17;

        return Math.min(scale, (5*scale)/(recipe.getFluidResults().size() + 2));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, @NotNull DistillationRecipe recipe, @NotNull IFocusGroup focuses) {
        int scale = getScale(recipe);

        var horizontal_marg = (177 - scale * (3 + recipe.getFluidResults().size()))/2;

        builder.addSlot(RecipeIngredientRole.INPUT, horizontal_marg, (int) (103.0 / 2 + (scale * (recipe.getFluidResults().size() - 5)) / 2.0))
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidIngredients().get(0).getMatchingFluidStacks()));

        for (int i = 0; i < recipe.getFluidResults().size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT,
                            (int) (horizontal_marg + (3 + i) * scale + 2 * scale / 3.0),
                            (int) (103.0 / 2 + scale / 2.0)
                    )
                    .setBackground(getRenderedSlot(), - 1, - 1)
                    .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(i)));
        }

//        for (int i = 0; i < recipe.getFluidResults().size(); i++) {
//            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, horizontal_marg + 3 * scale, (int) (103.0 / 2 + (scale * (recipe.getFluidResults().size() + 2)) / 2.0 - (2.7 + i)* scale))
//                    .setBackground(getRenderedSlot(), -1, -1)
//                    .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(i)));
//        }
    }

    @Override
    public void draw(@NotNull DistillationRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        int scale = getScale(recipe);

        var horizontal_marg = (177 - scale * (3 + recipe.getFluidResults().size()))/2;

        matrixStack.pushPose();

        matrixStack.pushPose();
        matrixStack.translate(horizontal_marg + scale/ 3.0, (int) (103.0 / 2 + (scale * (recipe.getFluidResults().size() -0.25)) / 2.0), 0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-90));

        GuiTextures.FLIPPED_DOWN_ARROW.render(matrixStack, 0, 0);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(horizontal_marg + 3 * scale + scale / 3.0, (int) (103.0 / 2 - (scale * 1.5) / 2.0), 0);

        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 0, 0);
        matrixStack.popPose();

        matrixStack.translate(horizontal_marg + 1.5 * scale, 103.0 / 2 + (scale * (recipe.getFluidResults().size() + 2)) / 2.0, 0);

        matrixStack.pushPose();
        matrixStack.translate(0, -1.600 * scale, -200);
        matrixStack.scale(scale/23f, scale/23f, scale/23f);

        heater.withHeat(recipe.getRequiredHeat().visualizeAsBlazeBurner())
                .draw(matrixStack, 0, 0);

        matrixStack.popPose();

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

        matrixStack.pushPose();
        matrixStack.translate(0, -scale, 0);

        AnimatedKinetics.defaultBlockElement(AllBlocks.FLUID_TANK.getDefaultState().setValue(
                        FluidTankBlock.SHAPE, FluidTankBlock.Shape.PLAIN
                ))
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();

        for (int i = 0; i < recipe.getFluidResults().size(); i++) {
            matrixStack.pushPose();
            matrixStack.translate(0, -(2 + i) * scale, 0);

            AnimatedKinetics.defaultBlockElement(AllBlocks.FLUID_TANK.getDefaultState().setValue(
                            FluidTankBlock.SHAPE, FluidTankBlock.Shape.PLAIN
                    ))
                    .atLocal(0, 0, 0)
                    .scale(scale)
                    .render(matrixStack);

            matrixStack.popPose();
        }


        matrixStack.popPose();
    }
}
