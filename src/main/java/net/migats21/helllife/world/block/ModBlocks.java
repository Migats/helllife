package net.migats21.helllife.world.block;

import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class ModBlocks extends Blocks {
    public static final Block DARK_BEACON = register("dark_beacon", new DarkBeaconBlock(BlockBehaviour.Properties.ofFullCopy(BEACON)));
    public static final Block END_AMETHYST_ORE = register("end_amethyst_ore", new DropExperienceBlock(UniformInt.of(0, 2), BlockBehaviour.Properties.ofFullCopy(END_STONE)));

    public static @NotNull Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(HellLife.MODID, name), block);
    }

}
