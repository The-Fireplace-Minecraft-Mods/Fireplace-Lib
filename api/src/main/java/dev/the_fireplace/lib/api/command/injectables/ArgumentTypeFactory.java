package dev.the_fireplace.lib.api.command.injectables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.the_fireplace.lib.api.command.interfaces.OfflineSupportedPlayerArgumentType;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;

public interface ArgumentTypeFactory
{
    @Deprecated
    OfflineSupportedPlayerArgumentType possiblyOfflinePlayer();

    @Deprecated
    PossiblyOfflinePlayer getPossiblyOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException;
}
