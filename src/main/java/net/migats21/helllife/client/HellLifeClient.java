package net.migats21.helllife.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.migats21.helllife.world.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;

public class HellLifeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DARK_BEACON, RenderType.translucent());
    }
}
