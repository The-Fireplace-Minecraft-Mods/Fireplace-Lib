package dev.the_fireplace.lib.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.lib.api.chat.TextPaginator;
import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.impl.command.HelpCommandFactoryImpl;
import net.minecraft.server.command.ServerCommandSource;

public interface HelpCommandFactory {
	static HelpCommandFactory getInstance() {
		//noinspection deprecation
		return HelpCommandFactoryImpl.INSTANCE;
	}

	HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase);
}
