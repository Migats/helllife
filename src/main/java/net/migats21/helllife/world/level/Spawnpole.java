package net.migats21.helllife.world.level;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.migats21.helllife.HellLife;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Spawnpole extends SavedData {
    private final BlockPos location;
    private final Set<UUID> bound = new HashSet<>();
    private static final Factory<Spawnpole> FACTORY = new Factory<>(Spawnpole::new, Spawnpole::new, DataFixTypes.LEVEL);
    public static final Component BOUND_MESSAGE = Component.translatableWithFallback("spawn.spawnpole.bound", "Your life is now bound to the spawn pole");
    public static final Component DESTROYED_MESSAGE = Component.translatableWithFallback("spawn.spawnpole.destroyed", "The spawn pole has been destroyed");
    public static final Component RESTORED_MESSAGE = Component.translatableWithFallback("spawn.spawnpole.restored", "The spawn pole has been restored");
    private static final Component LOAD_MESSAGE = Component.translatableWithFallback("spawn.spawnpole.load", "The spawnpole is now loaded");
    private static final Component UNLOAD_MESSAGE = Component.translatableWithFallback("spawn.spawnpole.unload", "The spawnpole is not loaded anymore");
    static final SimpleCommandExceptionType OVERWORLD_ERROR = new SimpleCommandExceptionType(Component.translatableWithFallback("commands.setspawnpole.fail.overworld", "You cannot set a spawnpole outside the overworld"));
    static final SimpleCommandExceptionType BEACON_ERROR = new SimpleCommandExceptionType(Component.translatableWithFallback("commands.setspawnpole.fail.beacon", "A spawnpole requires a beacon to be placed"));

    private Spawnpole() {
        this(null);
    }

    private Spawnpole(BlockPos blockPos) {
        location = blockPos instanceof BlockPos.MutableBlockPos ? new BlockPos(blockPos) : blockPos;
    }

    private Spawnpole(CompoundTag compoundTag, HolderLookup.Provider provider) {
        int i = compoundTag.getInt("ModDataType");
        if (i == 2) {
            location = NbtUtils.readBlockPos(compoundTag, "location").orElse(null);
            if (compoundTag.contains("bound", Tag.TAG_INT_ARRAY)) {
                IntArrayTag intArrayTag = (IntArrayTag) compoundTag.get("bound");
                for (int j=0;j<intArrayTag.size();j+=4) {
                    bound.add(UUIDUtil.uuidFromIntArray(intArrayTag.subList(j, j+4).stream().mapToInt(IntTag::getAsInt).toArray()));
                }
            }
        } else if (i < 2) {
            location = this.loadLegacy(compoundTag);
        } else {
            location = null;
            HellLife.LOGGER.warn("Spawn pole data is lost because it was saved for a newer version of spawn pole");
        }
    }

    private BlockPos loadLegacy(CompoundTag compoundTag) {
        if (compoundTag.contains("location", Tag.TAG_INT_ARRAY)) {
            ListTag listTag = compoundTag.getList("boundPlayers", Tag.TAG_INT_ARRAY);
            for (Tag tag : listTag) {
                bound.add(NbtUtils.loadUUID(tag));
            }
            int[] ints = compoundTag.getIntArray("location");
            return new BlockPos(ints[0], ints[1], ints[2]);
        }
        return null;
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayer player) {
                if (canEliminate(player)) {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, player.level());
                    lightningBolt.moveTo(player.position());
                    lightningBolt.setVisualOnly(true);
                    player.level().addFreshEntity(lightningBolt);
                    player.setGameMode(GameType.SPECTATOR);
                    player.connection.send(new ClientboundSetTitleTextPacket(Component.translatableWithFallback("deathScreen.eliminated", "You are eliminated").withStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
                }
            }
        });
        CommandRegistrationCallback.EVENT.register(SetSpawnpoleCommand::register);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("ModDataType", 2);
        if (location != null) {
            compoundTag.put("location", NbtUtils.writeBlockPos(location));
            IntArrayTag intArrayTag = new IntArrayTag(new int[0]);
            bound.forEach(uuid -> intArrayTag.addAll(NbtUtils.createUUID(uuid)));
            compoundTag.put("bound", intArrayTag);
        }
        return compoundTag;
    }

    public static Spawnpole create(ServerLevel level, BlockPos blockPos) {
        Spawnpole spawnpole = new Spawnpole(blockPos);
        level.getDataStorage().set("spawnpole", spawnpole);
        spawnpole.setDirty();
        return spawnpole;
    }

    public static Spawnpole get(ServerLevel level, BlockPos blockPos) {
        if (!ChallengeData.getSpawnDimension(level.getServer()).equals(level.dimension())) return null;
        Spawnpole spawnpole = level.getDataStorage().get(FACTORY, "spawnpole");
        return spawnpole != null && spawnpole.location.equals(blockPos) ? spawnpole : null;
    }

    private static boolean canEliminate(ServerPlayer player) {
        ServerLevel overworld = player.getServer().overworld();
        Spawnpole spawnpole = overworld.getDataStorage().get(FACTORY, "spawnpole");
        if (spawnpole == null || spawnpole.location == null) return false;
        if (!(overworld.isLoaded(spawnpole.location) && overworld.getBlockState(spawnpole.location).getBlock() instanceof BeaconBlock)) return true;
        return !spawnpole.bound.contains(player.getUUID());
    }

    public boolean bind(Player player) {
        setDirty();
        return bound.add(player.getUUID());
    }

    public void clear() {
        bound.clear();
        setDirty();
    }
}
