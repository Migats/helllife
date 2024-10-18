package net.migats21.helllife.common;

import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    public static Music MUSIC_BIOME_NETHER_DEATHLANDS = registerMusic(ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "music.nether.deathlands"));
    public static SoundEvent GENERIC_SHOCKWAVE = register(ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "entity.generic.shockwave"));
    public static final SoundEvent AMBIENT_SHOCKWAVE = register(ResourceLocation.fromNamespaceAndPath(HellLife.MODID, "ambient.shockwave"));

    private static SoundEvent register(ResourceLocation resourceLocation) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    private static Music registerMusic(ResourceLocation resourceLocation) {
        return Musics.createGameMusic(Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation)));
    }


    public static void register() {

    }
}
