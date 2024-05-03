package net.migats21.helllife.world.block.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.migats21.helllife.HellLife;
import net.migats21.helllife.world.spawn.Spawnpole;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DarkBeaconBlockEntity extends BlockEntity {
    private int lastCheckY;
    private ArrayList<BeaconBlockEntity.BeaconBeamSection> checkingBeamSections = new ArrayList<>();
    private int levels;
    private ArrayList<BeaconBlockEntity.BeaconBeamSection> beamSections = new ArrayList<>();
    public DarkBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.DARK_BEACON, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, DarkBeaconBlockEntity beaconBlockEntity) {
        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        BlockPos blockPos2;
        if (beaconBlockEntity.lastCheckY < j) {
            blockPos2 = blockPos;
            beaconBlockEntity.checkingBeamSections = Lists.newArrayList();
            beaconBlockEntity.lastCheckY = blockPos.getY() - 1;
        } else {
            blockPos2 = new BlockPos(i, beaconBlockEntity.lastCheckY + 1, k);
        }

        BeaconBlockEntity.BeaconBeamSection beaconBeamSection = beaconBlockEntity.checkingBeamSections.isEmpty() ? null : beaconBlockEntity.checkingBeamSections.get(beaconBlockEntity.checkingBeamSections.size() - 1);
        int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);

        int m;
        for(m = 0; m < 10 && blockPos2.getY() <= l; ++m) {
            BlockState blockState2 = level.getBlockState(blockPos2);
            Block block = blockState2.getBlock();
            if (block instanceof BeaconBeamBlock) {
                float[] fs = ((BeaconBeamBlock)block).getColor().getTextureDiffuseColors();
                if (beaconBlockEntity.checkingBeamSections.isEmpty()) {
                    beaconBeamSection = new BeaconBlockEntity.BeaconBeamSection(fs);
                    beaconBlockEntity.checkingBeamSections.add(beaconBeamSection);
                } else if (beaconBeamSection != null) {
                    if (Arrays.equals(fs, beaconBeamSection.getColor())) {
                        beaconBeamSection.increaseHeight();
                    } else {
                        beaconBeamSection = new BeaconBlockEntity.BeaconBeamSection(new float[]{(beaconBeamSection.getColor()[0] + fs[0]) / 2.0F, (beaconBeamSection.getColor()[1] + fs[1]) / 2.0F, (beaconBeamSection.getColor()[2] + fs[2]) / 2.0F});
                        beaconBlockEntity.checkingBeamSections.add(beaconBeamSection);
                    }
                }
            } else {
                if (beaconBeamSection == null || blockState2.getLightBlock(level, blockPos2) >= 15 && !blockState2.is(Blocks.BEDROCK)) {
                    beaconBlockEntity.checkingBeamSections.clear();
                    beaconBlockEntity.lastCheckY = l;
                    break;
                }

                beaconBeamSection.increaseHeight();
            }

            blockPos2 = blockPos2.above();
            ++beaconBlockEntity.lastCheckY;
        }

        m = beaconBlockEntity.levels;
        if (level.getGameTime() % 80L == 0L) {
            beaconBlockEntity.levels = updateBase(level, i, j, k);
            if (!level.isClientSide && Spawnpole.get((ServerLevel) level, blockPos) != null) {
                applyShockwave(level, blockPos, 6);
                BeaconBlockEntity.playSound(level, blockPos, SoundEvents.BEACON_AMBIENT);
            } else if (beaconBlockEntity.levels > 0 && !beaconBlockEntity.beamSections.isEmpty()) {
                applyShockwave(level, blockPos, beaconBlockEntity.levels);
                BeaconBlockEntity.playSound(level, blockPos, SoundEvents.BEACON_AMBIENT);
            }
        }

        if (beaconBlockEntity.lastCheckY >= l) {
            beaconBlockEntity.lastCheckY = level.getMinBuildHeight() - 1;
            boolean bl = m > 0;
            beaconBlockEntity.beamSections = beaconBlockEntity.checkingBeamSections;
            if (!level.isClientSide) {
                boolean bl2 = beaconBlockEntity.levels > 0;
                if (!bl && bl2) {
                    BeaconBlockEntity.playSound(level, blockPos, SoundEvents.BEACON_ACTIVATE);
                    List<ServerPlayer> entities = level.getEntitiesOfClass(ServerPlayer.class, (new AABB(i, j, k, i, j - 4, k)).inflate(10.0, 5.0, 10.0));
                    for (ServerPlayer serverPlayer : entities) {
                        CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverPlayer, beaconBlockEntity.levels);
                    }
                } else if (bl && !bl2) {
                    BeaconBlockEntity.playSound(level, blockPos, SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }

    }

    private static void applyShockwave(Level level, BlockPos blockPos, int levels) {
        // Not yet ready
/*
        if (level.isClientSide || level.dimension() != Level.NETHER) return;
        ServerLevel serverLevel = (ServerLevel) level;
        RandomSource randomSource = serverLevel.getRandomSequence(new ResourceLocation(HellLife.MODID, "deaths"));
        double d = randomSource.nextDouble() * (double)(levels * 12) + 28.0;
        AABB aabb = AABB.ofSize(new Vec3(blockPos.getX(), 128.0, blockPos.getZ()), d * 2.0, 256.0, d * 2.0);
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, aabb, (mob) ->
            blockPos.distToCenterSqr(mob.position()) < d*d && (levels < 5 ? mob instanceof Enemy : level.getBiome(mob.blockPosition()).is(ModBiomes.NETHER_DEATHLANDS))
        );
        int i = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        for (int j=0;j<i;j++) {
            if (mobs.isEmpty()) return;
            Mob mob = mobs.get(randomSource.nextInt(mobs.size()));
            mob.hurt(mob.damageSources().source(ModDamageTypes.SHOCKWAVE), Float.MAX_VALUE);
            if (mob.isDeadOrDying()) mobs.remove(mob);
            mob.playSound(ModSoundEvents.GENERIC_SHOCKWAVE, 2.0f, 1.0f);
            if (j == 0) level.playSound(null, blockPos, ModSoundEvents.AMBIENT_SHOCKWAVE, SoundSource.AMBIENT, 10.0f, 1.0f);
            AABB aabb1 = mob.getBoundingBox();
            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, mob.getX(), mob.getY(), mob.getZ(), 100, aabb1.getXsize(), aabb1.getYsize(), aabb1.getZsize(), 0.05);
        }
*/
    }

    private static int updateBase(Level level, int i, int j, int k) {
        int l = 0;

        for(int m = 1; m <= 4; l = m++) {
            int n = j - m;
            if (n < level.getMinBuildHeight()) {
                break;
            }

            boolean bl = true;

            for(int o = i - m; o <= i + m && bl; ++o) {
                for(int p = k - m; p <= k + m; ++p) {
                    if (!level.getBlockState(new BlockPos(o, n, p)).is(Blocks.NETHERITE_BLOCK)) {
                        bl = false;
                        break;
                    }
                }
            }

            if (!bl) {
                break;
            }
        }

        return l;
    }

    public void setRemoved() {
        BeaconBlockEntity.playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }


    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
        return this.levels == 0 ? ImmutableList.of() : this.beamSections;
    }
}
