package dev.the_fireplace.libtest.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.libtest.Tests;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FLTestCommand
{
    private final Tests tests;

    @Inject
    public FLTestCommand(Tests tests) {
        this.tests = tests;
    }

    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("fltest")
            .then(CommandManager.argument("suiteName", StringArgumentType.word())
                .executes(this::executeSuite)
            )
        );
    }

    private int executeSuite(CommandContext<ServerCommandSource> commandContext) {
        String suiteName = commandContext.getArgument("suiteName", String.class);
        String result = tests.execute(commandContext, suiteName);
        commandContext.getSource().sendFeedback(new LiteralText(result), false);
        return 1;
    }
}
