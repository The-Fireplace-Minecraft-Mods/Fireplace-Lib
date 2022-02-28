package dev.the_fireplace.libtest.setup;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;

public abstract class TestSuite
{
    public abstract void execute(CommandContext<ServerCommandSource> commandContext);

    protected CommandOutput getCommandOutput(CommandContext<ServerCommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();
        MinecraftServer server = commandContext.getSource().getServer();

        return entity != null ? entity : server;
    }
}
