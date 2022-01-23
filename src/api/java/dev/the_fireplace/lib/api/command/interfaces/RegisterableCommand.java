package dev.the_fireplace.lib.api.command.interfaces;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface RegisterableCommand
{
    CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher);
}
