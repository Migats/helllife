package net.migats21.helllife.world.level;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.BeaconBlock;

public class SetSpawnpoleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ignoredBuildContext, Commands.CommandSelection ignoredSelection) {
        dispatcher.register(Commands.literal("setspawnpole").requires(commandSourceStack -> commandSourceStack.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(SetSpawnpoleCommand::setSpawnpole)));
    }

    private static byte setSpawnpole(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        ServerLevel level = source.getLevel();
        if (!level.dimensionType().natural()) throw Spawnpole.OVERWORLD_ERROR.create();
        if (!(level.getBlockState(blockPos).getBlock() instanceof BeaconBlock)) throw Spawnpole.BEACON_ERROR.create();
        Spawnpole.create(level, blockPos);
        level.playSound(null, blockPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.0f);
        return 1;
    }
}
