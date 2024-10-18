package net.migats21.helllife.mixin;

import net.migats21.helllife.world.level.Spawnpole;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlock.class)
public abstract class MixinSpawnpole extends BaseEntityBlock {
    protected MixinSpawnpole(Properties properties) {
        super(properties);
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void activateSpawnpole(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide) return;
        Spawnpole spawnpole = Spawnpole.get((ServerLevel) level, blockPos);
        if (spawnpole == null) return;
        cir.cancel();
        cir.setReturnValue(InteractionResult.CONSUME);
        if (spawnpole.bind(player)) {
            ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD, blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, 20, 0.0, 1.0, 0.0, 0.1);
            player.sendSystemMessage(Spawnpole.BOUND_MESSAGE);
            level.playSound(null, blockPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.0f, 1.0f);
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
