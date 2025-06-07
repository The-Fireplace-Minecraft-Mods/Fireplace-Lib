package dev.the_fireplace.playtest.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.lib.api.teleport.injectables.Teleporter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Singleton
public final class TeleportUpCommand implements RegisterableCommand
{
    private final Requirements requirements;
    private final Teleporter teleporter;

    @Inject
    public TeleportUpCommand(
        Requirements requirements,
        Teleporter teleporter
    ) {
        this.requirements = requirements;
        this.teleporter = teleporter;
    }


    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("tpup")
            .requires(requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        ServerPlayer serverPlayer = command.getSource().getPlayerOrException();
        serverPlayer.sendSystemMessage(Component.literal("Teleport command received, moving up."));
        BlockPos targetPos = serverPlayer.blockPosition().above(5);
        teleporter.teleport(serverPlayer, serverPlayer.serverLevel(), targetPos);

        return Command.SINGLE_SUCCESS;
    }
}
