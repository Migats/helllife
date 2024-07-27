package net.migats21.helllife.world.structure;

import com.mojang.serialization.MapCodec;
import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class ModStructureTypes {
    public static final StructurePlacementType<CenteredStructurePlacement> CENTER_PLACEMENT = Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "center_placement"), () -> CenteredStructurePlacement.CODEC);
    public static final StructureType<FixedJigsawStructure> FIXED_JIGSAW = register(ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "fixed_jigsaw"), FixedJigsawStructure.CODEC);

    // noInspection SameParameterValue
    private static <S extends Structure> StructureType<S> register(ResourceLocation key, MapCodec<S> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, key, () -> codec);
    }

    public static void register() {

    }
}
