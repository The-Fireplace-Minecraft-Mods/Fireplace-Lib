package dev.the_fireplace.lib.impl.commandhelpers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.lib.api.chat.TextPaginator;
import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.api.command.HelpCommand;
import dev.the_fireplace.lib.api.command.HelpCommandFactory;
import net.minecraft.server.command.ServerCommandSource;

public final class HelpCommandFactoryImpl implements HelpCommandFactory {
	@Deprecated
	public static final HelpCommandFactory INSTANCE = new HelpCommandFactoryImpl();

	private HelpCommandFactoryImpl(){}

	@Override
	public HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase) {
		return new HelpCommandImpl(translator, textPaginator, modid, helpCommandBase);
	}
}
