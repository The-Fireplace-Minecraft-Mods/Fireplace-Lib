package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.interfaces.OfflineSupportedPlayerArgumentType;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.UUID;

@Implementation
@Singleton
public final class ArgumentTypeFactoryImpl implements ArgumentTypeFactory
{
    public static final OfflinePlayerArgumentType.Serializer OFFLINE_PLAYER_ARGUMENT_SERIALIZER = new OfflinePlayerArgumentType.Serializer();

    @Override
    public OfflineSupportedPlayerArgumentType possiblyOfflinePlayer() {
        return reader -> EntityArgument.player().parse(reader);
    }

    @Override
    public PossiblyOfflinePlayer getPossiblyOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        ServerPlayer serverPlayer = commandContext.getArgument(string, EntitySelector.class).findSinglePlayer(commandContext.getSource());
        return new PossiblyOfflinePlayer()
        {
            @Override
            public UUID getId() {
                return serverPlayer.getUUID();
            }

            @Override
            public String getName() {
                return serverPlayer.getName().getString();
            }

            @Nullable
            @Override
            public ServerPlayer entity() {
                return serverPlayer;
            }
        };
    }
}
