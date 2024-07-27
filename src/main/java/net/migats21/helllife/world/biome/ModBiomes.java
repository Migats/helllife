package net.migats21.helllife.world.biome;

import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public class ModBiomes {
    public static final ResourceKey<Biome> NETHER_DEATHLANDS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "nether_deathlands"));
    public static final ResourceKey<ConfiguredWorldCarver<?>> GHOSTDOME_HOLE = ResourceKey.create(Registries.CONFIGURED_CARVER, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "ghostdome_hole"));

    public static void register() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "nether"), NetherBiomeSource.CODEC);
        Registry.register(BuiltInRegistries.CARVER, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "ghostdome_carver"), new GhostDomeCarver(CaveCarverConfiguration.CODEC));
        ModBiomeTags.register();
    }
}
