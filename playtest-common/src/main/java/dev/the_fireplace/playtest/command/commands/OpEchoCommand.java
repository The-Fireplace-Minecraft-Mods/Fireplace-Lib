package dev.the_fireplace.playtest.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.playtest.PlaytestConstants;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@Singleton
public final class OpEchoCommand implements RegisterableCommand
{
    private final FeedbackSender feedbackSender;
    private final Requirements requirements;

    @Inject
    public OpEchoCommand(
        Requirements requirements,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory
    ) {
        this.feedbackSender = feedbackSenderFactory.get(translatorFactory.getTranslator(PlaytestConstants.MODID));
        this.requirements = requirements;
    }


    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("opecho")
            .requires(requirements::manageServer)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        feedbackSender.basic(command, "command.opecho.success");
        return Command.SINGLE_SUCCESS;
    }
}
