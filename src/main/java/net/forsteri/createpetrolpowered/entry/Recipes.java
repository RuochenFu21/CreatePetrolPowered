package net.forsteri.createpetrolpowered.entry;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.forsteri.createpetrolpowered.CreatePetrolPowered;
import net.forsteri.createpetrolpowered.content.distillation.DistillationRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum Recipes implements IRecipeTypeInfo {
    FRACTIONAL_DISTILLATION(DistillationRecipe::new);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    @SuppressWarnings("unused")
    Recipes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = CreatePetrolPowered.asResource(name);
        serializerObject = Recipes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Recipes.Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    Recipes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = CreatePetrolPowered.asResource(name);
        serializerObject = Recipes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Recipes.Registers.TYPE_REGISTER.register(name, () -> simpleType(id));
        type = typeObject;
    }

    Recipes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }


    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreatePetrolPowered.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CreatePetrolPowered.MODID);
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        Recipes.Registers.SERIALIZER_REGISTER.register(modEventBus);
        Recipes.Registers.TYPE_REGISTER.register(modEventBus);
    }
}
