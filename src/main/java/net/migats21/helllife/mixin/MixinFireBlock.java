package net.migats21.helllife.mixin;

import net.migats21.helllife.world.biome.ModBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class MixinFireBlock {
    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    public void checkBiome(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (level.getBiome(blockPos).is(ModBiomeTags.IS_BLACKLANDS)) {
            level.removeBlock(blockPos, false);
            ci.cancel();
        }
    }
}
