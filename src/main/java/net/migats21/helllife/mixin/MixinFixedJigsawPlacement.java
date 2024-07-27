package net.migats21.helllife.mixin;

import net.migats21.helllife.world.structure.FixedJigsawStructure;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JigsawPlacement.class)
public class MixinFixedJigsawPlacement {
    @Redirect(method = "addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;ILnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;)Ljava/util/Optional;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Rotation;getRandom(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/level/block/Rotation;"))
    private static Rotation redirectRotation(RandomSource randomSource) {
        if (FixedJigsawStructure.isPlacing) return Rotation.NONE;
        else return Rotation.getRandom(randomSource);
    }
}
