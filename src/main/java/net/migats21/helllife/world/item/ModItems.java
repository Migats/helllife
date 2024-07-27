package net.migats21.helllife.world.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class ModItems {
    public static final Item DARK_BEACON = register("dark_beacon", new BlockItem(ModBlocks.DARK_BEACON, new Item.Properties().rarity(Rarity.EPIC)));
    public static final Item END_AMETHYST_ORE = register("end_amethyst_ore", new BlockItem(ModBlocks.END_AMETHYST_ORE, new Item.Properties()));

    private static @NotNull Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name), item);
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
