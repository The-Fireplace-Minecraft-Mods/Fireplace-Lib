package dev.the_fireplace.lib.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.command.FeedbackSender;
import dev.the_fireplace.lib.api.command.RegisterableCommand;
import dev.the_fireplace.lib.api.command.Requirements;
import dev.the_fireplace.lib.api.storage.utility.ReloadableManager;
import dev.the_fireplace.lib.impl.FireplaceLib;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class FLReloadCommand implements RegisterableCommand {

    private final Requirements requirements;
    private final ReloadableManager reloadableManager;
    private final FeedbackSender feedbackSender;

    FLReloadCommand(Requirements requirements, FeedbackSender feedbackSender) {
        this.requirements = requirements;
        this.feedbackSender = feedbackSender;
        reloadableManager = ReloadableManager.getInstance();
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("flreload")
            .requires(requirements::manageServer)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> commandContext) {
        reloadableManager.reload(FireplaceLib.MODID);
        feedbackSender.basic(commandContext, "fireplacelib.command.reload.reloaded");
        return 1;
    }
}
