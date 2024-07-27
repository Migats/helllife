package net.migats21.helllife.world.structure;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CenteredStructurePlacement extends StructurePlacement {
    public static final MapCodec<CenteredStructurePlacement> CODEC = RecordCodecBuilder.mapCodec((instance) -> codec(instance).apply(instance, CenteredStructurePlacement::new));

    // TODO: Look at ConcentricRingsStructurePlacement to make sure the deprecated ExclusionZone will not cause problems
    private static Products.P8<RecordCodecBuilder.Mu<CenteredStructurePlacement>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>, Integer, Integer, HolderSet<Biome>> codec(RecordCodecBuilder.Instance<CenteredStructurePlacement> instance) {
        Products.P5<RecordCodecBuilder.Mu<CenteredStructurePlacement>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>> placementCodec = placementCodec(instance);
        Products.P3<RecordCodecBuilder.Mu<CenteredStructurePlacement>, Integer, Integer, HolderSet<Biome>> additionalCodec = instance.group(Codec.intRange(-1874999, 1874998).fieldOf("x_offset").forGetter(CenteredStructurePlacement::getX), Codec.intRange(-1874999, 1874998).fieldOf("z_offset").forGetter(CenteredStructurePlacement::getZ), RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("preferred_biomes").forGetter(CenteredStructurePlacement::getPreferredBiomes));
        return new Products.P8<>(placementCodec.t1(), placementCodec.t2(), placementCodec.t3(), placementCodec.t4(), placementCodec.t5(), additionalCodec.t1(), additionalCodec.t2(), additionalCodec.t3());
    }

    private final int x;
    private final int z;
    private final HolderSet<Biome> preferredBiomes;

    public CenteredStructurePlacement(Vec3i vec3i, FrequencyReductionMethod frequencyReductionMethod, float f, int i, Optional<ExclusionZone> optional, int j, int k, HolderSet<Biome> biomeHolderSet) {
        super(vec3i, frequencyReductionMethod, f, i, optional);
        this.x = j;
        this.z = k;
        this.preferredBiomes = biomeHolderSet;
    }

    public CenteredStructurePlacement(int i, int j, HolderSet<Biome> holderSet) {
        this(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty(), i, j, holderSet);
    }


    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int i, int j) {
        int k = SectionPos.sectionToBlockCoord(i);
        int l = SectionPos.sectionToBlockCoord(j);
        return i == x && j == z && preferredBiomes.contains(chunkGeneratorStructureState.biomeSource.getNoiseBiome(k, 0, l, chunkGeneratorStructureState.randomState().sampler()));
    }

    @Override
    public @NotNull StructurePlacementType<?> type() {
        return ModStructureTypes.CENTER_PLACEMENT;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public HolderSet<Biome> getPreferredBiomes() {
        return preferredBiomes;
    }
}
