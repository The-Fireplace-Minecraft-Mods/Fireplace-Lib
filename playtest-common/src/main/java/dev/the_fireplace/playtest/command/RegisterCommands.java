package dev.the_fireplace.playtest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.lib.api.command.injectables.HelpCommandFactory;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.command.commands.GetPlayerUUIDCommand;
import dev.the_fireplace.playtest.command.commands.OpEchoCommand;
import dev.the_fireplace.playtest.command.commands.PingCommand;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;

@Singleton
public final class RegisterCommands
{
    private final HelpCommandFactory helpCommandFactory;
    private final PingCommand pingCommand;
    private final OpEchoCommand opEchoCommand;
    private final GetPlayerUUIDCommand getPlayerUUIDCommand;

    @Inject
    public RegisterCommands(
        HelpCommandFactory helpCommandFactory,
        PingCommand pingCommand,
        OpEchoCommand opEchoCommand,
        GetPlayerUUIDCommand getPlayerUUIDCommand
    ) {
        this.helpCommandFactory = helpCommandFactory;
        this.pingCommand = pingCommand;
        this.opEchoCommand = opEchoCommand;
        this.getPlayerUUIDCommand = getPlayerUUIDCommand;
    }

    public void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        helpCommandFactory.create(
            PlaytestConstants.MODID,
            LiteralArgumentBuilder.literal("testhelp")
        ).addCommands(
            pingCommand.register(commandDispatcher),
            opEchoCommand.register(commandDispatcher),
            getPlayerUUIDCommand.register(commandDispatcher)
        ).register(commandDispatcher);
    }
}
