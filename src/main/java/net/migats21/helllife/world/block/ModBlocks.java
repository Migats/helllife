package net.migats21.helllife.world.block;

import net.migats21.helllife.HellLife;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModBlocks {
    public static final Block DARK_BEACON = register("dark_beacon", DarkBeaconBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON));
    public static final Block END_AMETHYST_ORE = register("end_amethyst_ore", (properties) -> new DropExperienceBlock(UniformInt.of(0, 2), properties), BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE));

    private static @NotNull Block register(String name, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties properties) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name);
        properties.setId(ResourceKey.create(Registries.BLOCK, resourceLocation));
        return Registry.register(BuiltInRegistries.BLOCK, resourceLocation, block.apply(properties));
    }

    public static void register() {

    }
}
