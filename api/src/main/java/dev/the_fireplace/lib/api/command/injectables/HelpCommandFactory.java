package dev.the_fireplace.lib.api.command.injectables;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.lib.api.command.interfaces.HelpCommand;
import net.minecraft.server.command.ServerCommandSource;

public interface HelpCommandFactory
{
    HelpCommand create(String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase);
}
