package net.migats21.helllife.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.tags.WorldPresetTags;

import java.util.concurrent.CompletableFuture;

public class ModWorldPresetTagsProvider extends WorldPresetTagsProvider {
    public ModWorldPresetTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(WorldPresetTags.NORMAL).add(HellLifeData.WORLD_PRESET);
    }
}
