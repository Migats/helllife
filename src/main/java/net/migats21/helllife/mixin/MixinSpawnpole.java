package net.migats21.helllife.mixin;

import net.migats21.helllife.world.item.recipe.GhostDomeRewardRecipe;
import net.migats21.helllife.world.item.recipe.ModRecipeTypes;
import net.migats21.helllife.world.level.ChallengeData;
import net.migats21.helllife.world.level.PlayerRewardData;
import net.migats21.helllife.world.level.Spawnpole;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BeaconBlock.class)
public abstract class MixinSpawnpole extends BaseEntityBlock {
    protected MixinSpawnpole(Properties properties) {
        super(properties);
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void activateSpawnpole(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide) return;
        ServerLevel serverLevel = (ServerLevel) level;
        Spawnpole spawnpole = Spawnpole.get(serverLevel, blockPos);
        if (spawnpole == null) return;
        cir.cancel();
        cir.setReturnValue(InteractionResult.CONSUME);
        if (spawnpole.bind(player)) {
            serverLevel.sendParticles(ParticleTypes.END_ROD, blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, 20, 0.0, 1.0, 0.0, 0.1);
            player.displayClientMessage(Spawnpole.BOUND_MESSAGE, false);
            level.playSound(null, blockPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        if (ChallengeData.isPresent(level.getServer()) && !player.getMainHandItem().isEmpty()) {
            if (!ChallengeData.canGetRewards(level.getServer())) {
                ((ServerPlayer)player).sendSystemMessage(Component.translatableWithFallback("message.helllife.reward.timeout", "Rewards are on timeout"), true);
            }
            ItemStack itemStack = player.getMainHandItem();
            SingleRecipeInput input = new SingleRecipeInput(player.getMainHandItem());
            PlayerRewardData rewardData = PlayerRewardData.getForPlayer((ServerPlayer) player);
            Optional<RecipeHolder< GhostDomeRewardRecipe>> recipeOpt = serverLevel.recipeAccess().getRecipeFor(ModRecipeTypes.GHOST_DOME_REWARD, input, level).filter(recipeHolder -> {
                int i = recipeHolder.value().getSupply();
                String name = recipeHolder.value().group();
                if (name.isEmpty()) name = recipeHolder.id().toString();
                return (i == 0 || i > rewardData.get(name));
            });
            if (recipeOpt.isPresent()) {
                ResourceLocation location = recipeOpt.get().id().location();
                GhostDomeRewardRecipe recipe = recipeOpt.get().value();
                String name = recipe.group();
                if (name.isEmpty()) name = location.toString();
                int i = rewardData.get(name), j = Math.min(recipe.getSupply() - i, itemStack.getCount() / recipe.getAmount());
                ItemStack result = recipe.assemble(input, level.registryAccess());
                result.setCount(result.getCount() * j);
                if (i == 0 && recipe.hasLore()) ((ServerPlayer) player).sendSystemMessage(Component.translatable(location.toLanguageKey("lore.reward")));
                level.getServer().executeIfPossible(() -> {
                    ItemEntity itemEntity = new ItemEntity(level, 0.0, 80.0, 0.0, result);
                    serverLevel.sendParticles(ParticleTypes.END_ROD, 0.0, 70.0, 0.0, 100, 0.0, 20.0, 0.0, 0.3);
                    level.addFreshEntity(itemEntity);
                });
                rewardData.add(name, j);
                ((ServerPlayer)player).sendSystemMessage(Component.translatableWithFallback("message.helllife.reward.success", "Your offer is accepted"), true);
            } else ((ServerPlayer)player).sendSystemMessage(Component.translatableWithFallback("message.helllife.reward.fail", "You gained nothing"), true);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide) {
            Spawnpole spawnpole = Spawnpole.get((ServerLevel) level, blockPos);
            if (spawnpole != null) {
                spawnpole.clear();
                level.getServer().getPlayerList().broadcastSystemMessage(Spawnpole.DESTROYED_MESSAGE, false);
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide) {
            Spawnpole spawnpole = Spawnpole.get((ServerLevel) level, blockPos);
            if (spawnpole != null) {
                spawnpole.clear();
                level.getServer().getPlayerList().broadcastSystemMessage(Spawnpole.RESTORED_MESSAGE, false);
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
}
