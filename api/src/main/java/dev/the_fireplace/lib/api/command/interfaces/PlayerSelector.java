package dev.the_fireplace.lib.api.command.interfaces;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public interface PlayerSelector
{
    PossiblyOfflinePlayer get(CommandSourceStack source) throws CommandSyntaxException;
}
