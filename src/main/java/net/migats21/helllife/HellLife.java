package net.migats21.helllife;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.migats21.helllife.world.spawn.Spawnpole;
import org.slf4j.Logger;

public class HellLife implements ModInitializer {
    public static final String MODID = "helllife";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        Spawnpole.init();
    }
}
