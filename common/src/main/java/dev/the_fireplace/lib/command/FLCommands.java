package dev.the_fireplace.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class FLCommands
{
    private final FLReloadCommand flReloadCommand;

    @Inject
    public FLCommands(FLReloadCommand flReloadCommand) {
        this.flReloadCommand = flReloadCommand;
    }

    public void register(MinecraftServer server) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        flReloadCommand.register(dispatcher);
    }
}
