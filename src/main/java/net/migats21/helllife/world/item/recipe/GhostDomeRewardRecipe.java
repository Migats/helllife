package net.migats21.helllife.world.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class GhostDomeRewardRecipe extends SingleItemRecipe {
    private final int amount;
    private final int supply;
    private final boolean announceLore;

    public GhostDomeRewardRecipe(String group, Ingredient ingredient, int amount, ItemStack result, int supply, boolean announceLore) {
        super(group, ingredient, result);
        this.amount = amount;
        this.supply = supply;
        this.announceLore = announceLore;
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRecipeTypes.GHOST_DOME_REWARD_SERIALIZER;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return ModRecipeTypes.GHOST_DOME_REWARD;
    }

    @Override
    public boolean matches(SingleRecipeInput singleRecipeInput, Level level) {
        return super.matches(singleRecipeInput, level) && singleRecipeInput.item().getCount() >= amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getSupply() {
        return supply;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.GHOST_DOME_REWARD_CATEGORY;
    }

    public boolean hasLore() {
        return announceLore;
    }

    public static class Serializer implements RecipeSerializer<GhostDomeRewardRecipe> {
        private final MapCodec<GhostDomeRewardRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, GhostDomeRewardRecipe> streamCodec;

        Serializer() {
            this.codec = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group),
                Ingredient.CODEC.fieldOf("offer").forGetter(SingleItemRecipe::input),
                Codec.INT.optionalFieldOf("amount", 1).forGetter(GhostDomeRewardRecipe::getAmount),
                Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, ItemStack.CODEC).fieldOf("reward").forGetter(SingleItemRecipe::result),
                Codec.INT.optionalFieldOf("supply", 0).forGetter(GhostDomeRewardRecipe::getSupply),
                Codec.BOOL.optionalFieldOf("lore", false).forGetter((recipe) -> recipe.announceLore)
            ).apply(instance, GhostDomeRewardRecipe::new));
            this.streamCodec = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group,
                Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input,
                ByteBufCodecs.INT, (recipe) -> recipe.amount,
                ItemStack.STREAM_CODEC, SingleItemRecipe::result,
                ByteBufCodecs.INT, (recipe) -> recipe.supply,
                ByteBufCodecs.BOOL, (recipe) -> recipe.announceLore,
                GhostDomeRewardRecipe::new
            );
        }

        @Override
        public MapCodec<GhostDomeRewardRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GhostDomeRewardRecipe> streamCodec() {
            return streamCodec;
        }
    }
}
