package net.migats21.helllife.world.block.entity;

import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypes {
    public static final BlockEntityType<DarkBeaconBlockEntity> DARK_BEACON = register("dark_beacon", BlockEntityType.Builder.of(DarkBeaconBlockEntity::new, ModBlocks.DARK_BEACON));

    private static  <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HellLife.MODID, name), builder.build(null));
    }

    public static void register() {

    }
}
