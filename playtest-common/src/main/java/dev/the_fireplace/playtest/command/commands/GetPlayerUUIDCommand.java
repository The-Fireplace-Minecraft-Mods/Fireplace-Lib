package dev.the_fireplace.playtest.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.playtest.PlaytestConstants;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@Singleton
public final class GetPlayerUUIDCommand implements RegisterableCommand
{
    private final FeedbackSender feedbackSender;
    private final Requirements requirements;
    private final ArgumentTypeFactory argumentTypeFactory;

    @Inject
    public GetPlayerUUIDCommand(
        Requirements requirements,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory,
        ArgumentTypeFactory argumentTypeFactory
    ) {
        this.feedbackSender = feedbackSenderFactory.get(translatorFactory.getTranslator(PlaytestConstants.MODID));
        this.requirements = requirements;
        this.argumentTypeFactory = argumentTypeFactory;
    }


    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> getPlayerIdCommand = Commands.literal("playerid")
            .requires(requirements::managePlayerAccess);

        getPlayerIdCommand.then(
            Commands.argument("player", argumentTypeFactory.possiblyOfflinePlayer())
                .suggests(argumentTypeFactory::listOfflinePlayerSuggestions)
                .executes(this::execute)
        );

        return commandDispatcher.register(getPlayerIdCommand);
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        PossiblyOfflinePlayer targetPlayer = argumentTypeFactory.getPossiblyOfflinePlayer(command, "player");
        String onlineStatus = targetPlayer.entity() != null ? "online" : "offline";
        feedbackSender.basic(command, "command.playerid.success", targetPlayer.getName(), targetPlayer.getId().toString(), onlineStatus);
        return Command.SINGLE_SUCCESS;
    }
}
