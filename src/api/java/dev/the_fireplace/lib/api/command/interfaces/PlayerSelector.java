package dev.the_fireplace.lib.api.command.interfaces;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

public interface PlayerSelector
{
    PossiblyOfflinePlayer get(ServerCommandSource source) throws CommandSyntaxException;
}
