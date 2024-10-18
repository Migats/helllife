package net.migats21.helllife.mixin;

import net.migats21.helllife.world.level.ChallengeData;
import net.migats21.helllife.world.level.Spawnpole;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Unit;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinInitSpawn {
    @Shadow public abstract @Nullable ServerLevel getLevel(ResourceKey<Level> resourceKey);

    @Shadow public abstract ServerLevel overworld();

    @Inject(method = "loadLevel", at = @At("TAIL"))
    public void loadSpawnAndCheck(CallbackInfo ci) {
        ServerLevel nether = getLevel(Level.NETHER);
        ChallengeData challengeData = ChallengeData.initChallengeData(nether);
        if (challengeData == null) return;
        overworld().getChunkSource().removeRegionTicket(TicketType.START, new ChunkPos(overworld().getSharedSpawnPos()), overworld().lastSpawnChunkRadius, Unit.INSTANCE);
        overworld().setDefaultSpawnPos(new BlockPos(0, 62, -12), 180.0f);
        if (!challengeData.isInitialized()) {
            Spawnpole.create(nether, new BlockPos(-1, 65, 0));
        }
    }
}
