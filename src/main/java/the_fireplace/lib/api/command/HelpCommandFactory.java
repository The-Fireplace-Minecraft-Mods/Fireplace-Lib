package the_fireplace.lib.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.lib.api.chat.TextPaginator;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.impl.command.HelpCommandFactoryImpl;

public interface HelpCommandFactory {
	static HelpCommandFactory getInstance() {
		//noinspection deprecation
		return HelpCommandFactoryImpl.INSTANCE;
	}

	HelpCommand create(Translator translator, TextPaginator textPaginator, String modid, LiteralArgumentBuilder<ServerCommandSource> helpCommandBase);
}
