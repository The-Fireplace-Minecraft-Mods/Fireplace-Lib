package dev.the_fireplace.lib.api.command.interfaces;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;

public interface RegisterableCommand
{
    CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher);
}
