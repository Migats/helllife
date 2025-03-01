package net.migats21.helllife.world.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {
    public static final Item DARK_BEACON = register("dark_beacon", (properties) -> new BlockItem(ModBlocks.DARK_BEACON, properties), new Item.Properties().rarity(Rarity.EPIC).useBlockDescriptionPrefix());
    public static final Item END_AMETHYST_ORE = register("end_amethyst_ore", (properties) -> new BlockItem(ModBlocks.END_AMETHYST_ORE, properties), new Item.Properties().useBlockDescriptionPrefix());

    private static @NotNull Item register(String name, Function<Item.Properties, Item> item, Item.Properties properties) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name);
        properties.setId(ResourceKey.create(Registries.ITEM, resourceLocation));
        return Registry.register(BuiltInRegistries.ITEM, resourceLocation, item.apply(properties));
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((entries) ->
            entries.addAfter(Items.BEACON, DARK_BEACON)
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register((entries) ->
            entries.addAfter(Items.NETHER_QUARTZ_ORE, END_AMETHYST_ORE)
        );
    }
}
