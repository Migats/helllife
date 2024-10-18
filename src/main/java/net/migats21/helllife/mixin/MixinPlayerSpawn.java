package net.migats21.helllife.mixin;

import net.migats21.helllife.world.level.ChallengeData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class MixinPlayerSpawn {
    @Shadow public abstract ServerLevel serverLevel();

    @Shadow @Final public MinecraftServer server;

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"))
    public ResourceKey<Level> initRespawnDimension() {
        return serverLevel().dimension();
    }

    @Redirect(method = "setRespawnPosition", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"))
    public ResourceKey<Level> resetSpawnPosition() {
        return ChallengeData.getSpawnDimension(server);
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"))
    public ResourceKey<Level> loadRespawnPosition() {
        return ChallengeData.getSpawnDimension(server);
    }

    @Redirect(method = "findRespawnPositionAndUseSpawnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel findFallbackRespawn(MinecraftServer server) {
        return server.getLevel(ChallengeData.getSpawnDimension(server));
    }
}
