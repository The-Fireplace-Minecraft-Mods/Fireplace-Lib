package the_fireplace.lib.impl.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.lib.api.chat.TextPaginator;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.command.HelpCommand;
import the_fireplace.lib.api.command.HelpCommandFactory;

public final class HelpCommandFactoryImpl implements HelpCommandFactory {
	@Deprecated
	public static final HelpCommandFactory INSTANCE = new HelpCommandFactoryImpl();

	private HelpCommandFactoryImpl(){}

	@Override
	public HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase) {
		return new HelpCommandImpl(translator, textPaginator, modid, helpCommandBase);
	}
}
