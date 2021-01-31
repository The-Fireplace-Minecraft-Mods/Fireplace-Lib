package the_fireplace.lib.api.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;

public interface FeedbackSender {
	int throwFailure(CommandContext<ServerCommandSource> command, String translationKey, Object... args) throws CommandException;

	void basic(CommandContext<ServerCommandSource> command, String translationKey, Object... args);

	void basic(ServerPlayerEntity targetPlayer, String translationKey, Object... args);

	void styled(CommandContext<ServerCommandSource> command, Style style, String translationKey, Object... args);

	void styled(ServerPlayerEntity targetPlayer, Style style, String translationKey, Object... args);
}
