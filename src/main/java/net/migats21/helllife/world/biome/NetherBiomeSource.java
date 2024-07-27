package net.migats21.helllife.world.biome;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class NetherBiomeSource extends MultiNoiseBiomeSource {
    private static final MapCodec<Holder<Biome>> ENTRY_CODEC = Biome.CODEC.fieldOf("biome");
    public static final MapCodec<Climate.ParameterList<Holder<Biome>>> DIRECT_CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes");
    private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
    private static final MapCodec<Holder<Biome>> NETHER_DEATHLANDS_CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(RegistryOps.retrieveElement(ModBiomes.NETHER_DEATHLANDS)).apply(instance, i -> i)
    );
    public static final MapCodec<NetherBiomeSource> CODEC = Codec.mapPair(Codec.mapEither(DIRECT_CODEC, PRESET_CODEC), NETHER_DEATHLANDS_CODEC).xmap((pair) -> new NetherBiomeSource(pair.getFirst(), pair.getSecond()), (netherBiomeSource) -> Pair.of(netherBiomeSource.parameters, netherBiomeSource.netherDeathLands));
    private final Holder<Biome> netherDeathLands;

    public NetherBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> either, Holder<Biome> netherDeathLands) {
        super(either);
        this.netherDeathLands = netherDeathLands;
    }

    @Override
    protected @NotNull MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int i, int j, int k, Climate.Sampler sampler) {
        int l = QuartPos.toBlock(i);
        int m = QuartPos.toBlock(j);
        int n = QuartPos.toBlock(k);
        int o = SectionPos.blockToSectionCoord(l);
        int p = SectionPos.blockToSectionCoord(n);
        if ((long)l * (long)l + (long)n * (long)n <= 10000L) {
            return this.netherDeathLands;
        } else {
            return this.getNoiseBiome(sampler.sample(i, j, k));
        }
    }

    @Override
    public @NotNull Set<Holder<Biome>> possibleBiomes() {
        HashSet<Holder<Biome>> set = Sets.newHashSet(super.possibleBiomes());
        set.add(this.netherDeathLands);
        return ImmutableSet.copyOf(set);
    }
}
