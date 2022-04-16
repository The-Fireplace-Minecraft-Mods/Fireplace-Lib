package dev.the_fireplace.lib.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

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
        CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();

        flReloadCommand.register(dispatcher);
    }
}
