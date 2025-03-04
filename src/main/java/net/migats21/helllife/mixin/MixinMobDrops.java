package net.migats21.helllife.mixin;

import net.migats21.helllife.common.ModDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(Mob.class)
public abstract class MixinMobDrops extends LivingEntity {

    @Shadow private Optional<ResourceKey<LootTable>> lootTable;

    protected MixinMobDrops(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void dropFromLootTable(ServerLevel level, DamageSource damageSource, boolean bl) {
        if (!damageSource.is(ModDamageTypes.SHOCKWAVE)) super.dropFromLootTable(level, damageSource, bl);
        lootTable = Optional.empty();
    }
}
