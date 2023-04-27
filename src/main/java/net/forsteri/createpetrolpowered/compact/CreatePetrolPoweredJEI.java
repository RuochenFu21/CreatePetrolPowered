package net.forsteri.createpetrolpowered.compact;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.config.CRecipes;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.forsteri.createpetrolpowered.CreatePetrolPowered;
import net.forsteri.createpetrolpowered.content.distillation.recipe.DistillationCategory;
import net.forsteri.createpetrolpowered.content.distillation.recipe.DistillationRecipe;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JeiPlugin
public class CreatePetrolPoweredJEI implements IModPlugin {
    public IIngredientManager ingredientManager;

    final List<CreateRecipeCategory<?>> ALL = new ArrayList<>();

    @SuppressWarnings("unused")
    private void loadCategories() {
        ALL.clear();

        CreateRecipeCategory<?> distillation = builder(DistillationRecipe.class)
                .addTypedRecipes(Recipes.FRACTIONAL_DISTILLATION)
                .catalyst(AllBlocks.FLUID_TANK::get)
                .catalyst(Registration.DISTILLATION_TANK_BLOCK::get)
                .itemIcon(Registration.DISTILLATION_TANK_BLOCK.get())
                .emptyBackground(177, 103)
                .build("fractional_distillation", DistillationCategory::new);

    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(CreatePetrolPowered.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        ALL.forEach(registration::addRecipeCategories);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();
        ALL.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        ALL.forEach(c -> c.registerCatalysts(registration));
    }

    @SuppressWarnings("SameParameterValue")
    private <T extends Recipe<?>> CreatePetrolPoweredJEI.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CreatePetrolPoweredJEI.CategoryBuilder<>(recipeClass);
    }

    @SuppressWarnings("UnusedReturnValue")
    private class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private final Predicate<CRecipes> predicate = cRecipes -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get()
                    .asItem()));
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> itemIcon(ItemLike item) {
            icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CreatePetrolPoweredJEI.CategoryBuilder<T> emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(AllConfigs.SERVER.recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            } else {
                //noinspection Convert2MethodRef
                recipesSupplier = () -> Collections.emptyList();
            }

            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                    new mezz.jei.api.recipe.RecipeType<>(new ResourceLocation(CreatePetrolPowered.MODID, name), recipeClass),
                    Lang.translateDirect("recipe." + name), background, icon, recipesSupplier, catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            ALL.add(category);
            return category;
        }
    }
}
