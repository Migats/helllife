package net.migats21.helllife.world.item.recipe;

import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {
    public static final RecipeType<GhostDomeRewardRecipe> GHOST_DOME_REWARD = register("ghost_dome_reward");
    public static final RecipeBookCategory GHOST_DOME_REWARD_CATEGORY = registerCategory("ghost_dome_reward");
    public static final RecipeSerializer<GhostDomeRewardRecipe> GHOST_DOME_REWARD_SERIALIZER = registerSerializer("ghost_dome_reward", new GhostDomeRewardRecipe.Serializer());

    private static <T extends Recipe<?>> RecipeType<T> register(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name), new RecipeType<T>() {
            @Override
            public String toString() {
            return ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name).toString();
            }
        });
    }

    private static RecipeBookCategory registerCategory(String name) {
        return Registry.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name), new RecipeBookCategory());
    }


    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name), serializer);
    }

    public static void register() {

    }
}
