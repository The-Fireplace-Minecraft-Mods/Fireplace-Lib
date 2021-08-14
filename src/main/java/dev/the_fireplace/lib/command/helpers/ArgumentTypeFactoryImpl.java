package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.interfaces.OfflineSupportedPlayerArgumentType;
import dev.the_fireplace.lib.api.command.interfaces.PlayerSelector;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class ArgumentTypeFactoryImpl implements ArgumentTypeFactory {
    @Override
    public OfflineSupportedPlayerArgumentType possiblyOfflinePlayer() {
        return new OfflinePlayerArgumentType();
    }

    @Override
    public PossiblyOfflinePlayer getPossiblyOfflinePlayer(CommandContext<ServerCommandSource> commandContext, String string) throws CommandSyntaxException {
        return commandContext.getArgument(string, PlayerSelector.class).get(commandContext.getSource());
    }
}
