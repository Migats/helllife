package net.migats21.helllife.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class FixedJigsawStructure extends JigsawStructure {

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final List<PoolAliasBinding> poolAliases;
    private final DimensionPadding dimensionPadding;
    private final LiquidSettings liquidSettings;
    public static boolean isPlacing = false;

    @SuppressWarnings("all")
    public static final MapCodec<FixedJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(settingsCodec(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((jigsawStructure) -> jigsawStructure.startPool), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((jigsawStructure) -> {
            return jigsawStructure.startJigsawName;
        }), Codec.intRange(0, 20).fieldOf("size").forGetter((jigsawStructure) -> jigsawStructure.maxDepth), HeightProvider.CODEC.fieldOf("start_height").forGetter((jigsawStructure) -> {
            return jigsawStructure.startHeight;
        }), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((jigsawStructure) -> {
            return jigsawStructure.useExpansionHack;
        }), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((jigsawStructure) -> {
            return jigsawStructure.projectStartToHeightmap;
        }), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((jigsawStructure) -> {
            return jigsawStructure.maxDistanceFromCenter;
        }), Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter((jigsawStructure) -> {
            return jigsawStructure.poolAliases;
        }), DimensionPadding.CODEC.optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING).forGetter((jigsawStructure) -> {
            return jigsawStructure.dimensionPadding;
        }), LiquidSettings.CODEC.optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter((jigsawStructure) -> {
            return jigsawStructure.liquidSettings;
        })).apply(instance, FixedJigsawStructure::new);
    });


    public FixedJigsawStructure(Structure.StructureSettings structureSettings, Holder<StructureTemplatePool> holder, Optional<ResourceLocation> optional, int i, HeightProvider heightProvider, boolean bl, Optional<Heightmap.Types> optional2, int j, List<PoolAliasBinding> list, DimensionPadding dimensionPadding, LiquidSettings liquidSettings) {
        super(structureSettings, holder, optional, i, heightProvider, bl, optional2, j, list, dimensionPadding, liquidSettings);
        this.startPool = holder;
        this.startJigsawName = optional;
        this.maxDepth = i;
        this.startHeight = heightProvider;
        this.useExpansionHack = bl;
        this.projectStartToHeightmap = optional2;
        this.maxDistanceFromCenter = j;
        this.poolAliases = list;
        this.dimensionPadding = dimensionPadding;
        this.liquidSettings = liquidSettings;
    }

    public FixedJigsawStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> holder, int i, HeightProvider heightProvider, boolean bl, Heightmap.Types types) {
        this(structureSettings, holder, Optional.empty(), i, heightProvider, bl, Optional.of(types), 80, List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS);
    }

    public FixedJigsawStructure(StructureSettings structureSettings, Holder<StructureTemplatePool> holder, int i, HeightProvider heightProvider, boolean bl) {
        this(structureSettings, holder, Optional.empty(), i, heightProvider, bl, Optional.empty(), 80, List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS);
    }

    private static DataResult<FixedJigsawStructure> verifyRange(FixedJigsawStructure jigsawStructure) {
        int i = switch (jigsawStructure.terrainAdaptation()) {
            case NONE -> 0;
            case BURY, BEARD_THIN, BEARD_BOX -> 12;
            default -> throw new IncompatibleClassChangeError("Program failed because Mojang changed the code");
        };

        return jigsawStructure.maxDistanceFromCenter + i > 128 ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128") : DataResult.success(jigsawStructure);
    }


    @Override
    public @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext generationContext) {
        ChunkPos chunkPos = generationContext.chunkPos();
        isPlacing = true;
        Optional<GenerationStub> optional = super.findGenerationPoint(generationContext);
        isPlacing = false;
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        return optional.map((stub) -> new GenerationStub(new BlockPos(i, stub.position().getY(), j), stub.generator()));
    }

    @Override
    public @NotNull StructureType<?> type() {
        return ModStructureTypes.FIXED_JIGSAW;
    }
}
