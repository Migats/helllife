package net.migats21.helllife.world.biome;

import net.migats21.helllife.HellLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagKey<Biome> IS_BLACKLANDS = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "is_blacklands"));
    public static final TagKey<Biome> HAS_GHOSTDOME = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "has_structure/ghostdome"));

    public static void register() {

    }
}
