package dev.the_fireplace.lib.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.command.RegisterableCommand;
import dev.the_fireplace.lib.api.command.Requirements;
import dev.the_fireplace.lib.api.storage.utility.ReloadableManager;
import dev.the_fireplace.lib.impl.FireplaceLib;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class FLReloadCommand implements RegisterableCommand {

    private final Requirements requirements;
    private final ReloadableManager reloadableManager;

    FLReloadCommand(Requirements requirements) {
        this.requirements = requirements;
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
        return 1;
    }
}
