package net.migats21.helllife.world.level;

import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.block.ModBlocks;
import net.migats21.helllife.world.block.entity.DarkBeaconBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChallengeData extends SavedData {
    private static final Factory<ChallengeData> FACTORY = new Factory<>(ChallengeData::new, ChallengeData::new, DataFixTypes.LEVEL);

    private boolean initialized;

    public ChallengeData() {
    }

    public ChallengeData(CompoundTag compoundTag, HolderLookup.Provider provider) {
        initialized = true;
    }

    public static ResourceKey<Level> getSpawnDimension(MinecraftServer server) {
        return server.getLevel(Level.NETHER).getDataStorage().get(FACTORY, HellLife.MODID) == null ? Level.OVERWORLD : Level.NETHER;
    }

    public static boolean isPresent(MinecraftServer server) {
        return server.getLevel(Level.NETHER).getDataStorage().get(FACTORY, HellLife.MODID) != null;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return compoundTag;
    }

    public static @Nullable ChallengeData initChallengeData(ServerLevel level) {
        if (level == null || !level.dimensionType().ultraWarm()) throw new IllegalCallerException("Attempted to access nether data from " + level.dimension().registry());
        ChallengeData challengeData = level.getDataStorage().get(FACTORY, HellLife.MODID);
        if (challengeData == null && level.getBlockState(new BlockPos(-1, 65, 0)).is(ModBlocks.DARK_BEACON)) {
            level.getChunkSource().addRegionTicket(TicketType.UNKNOWN, ChunkPos.ZERO, 2, ChunkPos.ZERO);
            BlockEntity entity = level.getBlockEntity(new BlockPos(-1, 65, 0));
            if (entity instanceof DarkBeaconBlockEntity darkBeaconBlockEntity && darkBeaconBlockEntity.isGenerated()) {
                challengeData = new ChallengeData();
                level.getDataStorage().set(HellLife.MODID, challengeData);
                challengeData.setDirty();
            }
            level.getChunkSource().removeRegionTicket(TicketType.UNKNOWN, ChunkPos.ZERO, 2, ChunkPos.ZERO);
        }
        return challengeData;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
