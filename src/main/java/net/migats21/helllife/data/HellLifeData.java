package net.migats21.helllife.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.biome.ModBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

public class HellLifeData implements DataGeneratorEntrypoint {
    // defining our registry key. this key provides an Identifier for our preset, that we can use for our lang files and data elements.
    @Unique
    public static final ResourceKey<WorldPreset> WORLD_PRESET = ResourceKey.create(Registries.WORLD_PRESET, ResourceLocation.fromNamespaceAndPath("helllife", "challenge"));
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        ModBiomes.register();
        pack.addProvider(ModWorldPresetTagsProvider::new);
    }

    @Override
    public @Nullable String getEffectiveModId() {
        return HellLife.MODID;
    }
}
