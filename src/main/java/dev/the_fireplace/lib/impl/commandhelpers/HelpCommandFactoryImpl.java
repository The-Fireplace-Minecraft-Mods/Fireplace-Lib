package dev.the_fireplace.lib.impl.commandhelpers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.chat.TextPaginator;
import dev.the_fireplace.lib.api.chat.internal.Translator;
import dev.the_fireplace.lib.api.command.HelpCommand;
import dev.the_fireplace.lib.api.command.HelpCommandFactory;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class HelpCommandFactoryImpl implements HelpCommandFactory {
	@Override
	public HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase) {
		return new HelpCommandImpl(translator, textPaginator, modid, helpCommandBase);
	}
}
