package net.migats21.helllife;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.migats21.helllife.world.biome.ModBiomes;
import net.migats21.helllife.world.block.ModBlocks;
import net.migats21.helllife.world.block.entity.ModBlockEntityTypes;
import net.migats21.helllife.world.item.ModItems;
import net.migats21.helllife.world.level.Spawnpole;
import net.migats21.helllife.world.structure.ModStructureTypes;
import org.slf4j.Logger;

public class HellLife implements ModInitializer {
    public static final String MODID = "helllife";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        ModBlockEntityTypes.register();
        ModStructureTypes.register();
        ModBiomes.register();
        Spawnpole.init();
    }
}
