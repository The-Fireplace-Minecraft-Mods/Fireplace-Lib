package dev.the_fireplace.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FLReloadCommand implements RegisterableCommand
{
    private final Requirements requirements;
    private final ReloadableManager reloadableManager;
    private final FeedbackSender feedbackSender;

    @Inject
    public FLReloadCommand(
        Requirements requirements,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory,
        ReloadableManager reloadableManager
    ) {
        Translator translator = translatorFactory.getTranslator(FireplaceLibConstants.MODID);
        this.feedbackSender = feedbackSenderFactory.get(translator);
        this.requirements = requirements;
        this.reloadableManager = reloadableManager;
    }

    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("flreload")
            .requires(requirements::manageServer)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> commandContext) {
        reloadableManager.reload(FireplaceLibConstants.MODID);
        feedbackSender.basic(commandContext, "fireplacelib.command.reload.reloaded");
        return 1;
    }
}
