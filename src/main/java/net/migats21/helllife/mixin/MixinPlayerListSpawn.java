package net.migats21.helllife.mixin;

import net.migats21.helllife.world.level.ChallengeData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class MixinPlayerListSpawn {
    @Shadow @Final private MinecraftServer server;

    @Redirect(method = "placeNewPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"))
    public ResourceKey<Level> placeNewPlayerIn() {
        return ChallengeData.getSpawnDimension(server);
    }

    @Redirect(method = "getPlayerForLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel getPlayerDimensionForLogin(MinecraftServer server) {
        return server.getLevel(ChallengeData.getSpawnDimension(server));
    }

    @ModifyArg(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
    public String newFallbackMessage(String string) {
        return ChallengeData.isPresent(server) ? string.replace("overworld", "nether") : string;
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel getPlayerFallbackDimension(MinecraftServer server) {
        return server.getLevel(ChallengeData.getSpawnDimension(server));
    }
}
