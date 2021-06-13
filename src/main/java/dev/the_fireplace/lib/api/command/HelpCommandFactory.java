package dev.the_fireplace.lib.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.lib.api.chat.TextPaginator;
import dev.the_fireplace.lib.api.chat.internal.Translator;
import net.minecraft.server.command.ServerCommandSource;

public interface HelpCommandFactory {
	HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase);
}
