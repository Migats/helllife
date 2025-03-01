package net.migats21.helllife.world.level;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class PlayerRewardData extends SavedData {
    private static final Factory<PlayerRewardData> FACTORY = new Factory<>(PlayerRewardData::new, PlayerRewardData::new, DataFixTypes.PLAYER);

    private final CompoundTag data = new CompoundTag();

    private PlayerRewardData() {

    }

    private PlayerRewardData(CompoundTag compoundTag, HolderLookup.Provider provider) {
        data.merge(compoundTag);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return data.merge(compoundTag);
    }

    public static PlayerRewardData getForPlayer(ServerPlayer player) {
        return player.server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(FACTORY, "ghostdome_reward_data/" + player.getStringUUID());
    }

    public void add(String string, int i) {
        int j = data.getByte(string) & 255;
        j+=i;
        if (j > 255) j = 255;
        data.putByte(string, (byte)j);
        setDirty();
    }

    public int get(String string) {
        return data.getByte(string) & 255;
    }
}
