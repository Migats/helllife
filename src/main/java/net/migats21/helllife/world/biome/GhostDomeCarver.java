package net.migats21.helllife.world.biome;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public class GhostDomeCarver extends WorldCarver<CaveCarverConfiguration> {
    public GhostDomeCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);
        this.liquids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
    }

    @Override
    public boolean carve(CarvingContext carvingContext, CaveCarverConfiguration carverConfiguration, ChunkAccess chunkAccess, Function<BlockPos, Holder<Biome>> biomeMap, RandomSource randomSource, Aquifer aquifer, ChunkPos chunkPos, CarvingMask carvingMask) {
        if (chunkPos.x != 0 && chunkPos.z != 0) return false;
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 35.0, 76.0, 6.0, 24, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 32.0, 64.0, 10.0, 24, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 32.0, 60.0, 11.0, 10, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 32.0, 56.0, 11.0, 20, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 32.0, 52.0, 11.0, 8, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 33.0, 48.0, 11.0, 16, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 33.0, 44.0, 12.0, 6, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 34.0, 40.0, 12.0, 12, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 35.0, 32.0, 13.0, 8, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 36.0, 24.0, 13.0, 4, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 37.0, 16.0, 14.0, 2, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 38.0, 6.0, 15.0, 2, carvingMask);
        carveEllipsoid(carvingContext, carverConfiguration, chunkAccess, biomeMap, aquifer, 0.0, 44.0, 0.0, 20.0, 20.0, carvingMask, GhostDomeCarver::shouldSkip);
        carveEllipsoid(carvingContext, carverConfiguration, chunkAccess, biomeMap, aquifer, 0.0, 72.0, 0.0, 20.0, 20.0, carvingMask, GhostDomeCarver::shouldSkip);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 76.0, 6.0, 15.0, 4, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 77.0, 16.0, 14.0, 4, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 78.0, 24.0, 13.0, 8, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 82.0, 32.0, 13.0, 8, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 86.0, 40.0, 12.0, 12, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 90.0, 44.0, 12.0, 6, carvingMask);
        carveRing(carvingContext, carverConfiguration, chunkAccess, biomeMap, randomSource, aquifer, 92.0, 48.0, 11.0, 16, carvingMask);
        return true;
    }

    private void carveRing(CarvingContext carvingContext, CaveCarverConfiguration carverConfiguration, ChunkAccess chunkAccess, Function<BlockPos, Holder<Biome>> function, RandomSource randomSource, Aquifer aquifer, double d, double e, double f, int i, CarvingMask carvingMask) {
        for (int j = 0; j < i; j++) {
            double g = (double)j * Math.PI * 2 / (double)i + randomSource.nextDouble();
            double h = Math.cos(g) * e, k = Math.sin(g) * e;
            carveEllipsoid(carvingContext, carverConfiguration, chunkAccess, function, aquifer, h, d, k, f - randomSource.nextDouble()*5.0, f, carvingMask, GhostDomeCarver::shouldSkip);
        }
    }

    @Override
    public boolean isStartChunk(CaveCarverConfiguration carverConfiguration, RandomSource randomSource) {
        return true;
    }

    private static boolean torusShouldSkip(CarvingContext context, double d, double e, double f, double g) {
        double h = 0.5 - Math.sqrt(d * d + f * f);
        double i = e * e;
        return i + h * h >= 0.25 && (h <= 0.0 || i >= 0.25);
    }

    private static boolean shouldSkip(CarvingContext context, double d, double e, double f, double g) {
        return d * d + e * e + f * f >= 1.0;
    }
}
