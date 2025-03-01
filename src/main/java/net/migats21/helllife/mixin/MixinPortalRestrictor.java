package net.migats21.helllife.mixin;

import net.migats21.helllife.world.biome.ModBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class MixinPortalRestrictor {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    public void randomCheck(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (serverLevel.getBiome(blockPos).is(ModBiomeTags.IS_BLACKLANDS)) {
            serverLevel.removeBlock(blockPos, false);
            ci.cancel();
        }
    }
}
