package the_fireplace.lib.impl.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import the_fireplace.lib.api.chat.TextStyles;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.command.FeedbackSender;

public final class SendFeedback implements FeedbackSender {

    private final Translator translator;

    SendFeedback(Translator translator) {
        this.translator = translator;
    }

    @Override
    public int throwFailure(CommandContext<ServerCommandSource> command, String translationKey, Object... args) throws CommandException {
        throw new CommandException(translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(TextStyles.RED));
    }

    @Override
    public void basic(CommandContext<ServerCommandSource> command, String translationKey, Object... args) {
        command.getSource().sendFeedback(translator.getTextForTarget(command.getSource(), translationKey, args), false);
    }

    @Override
    public void basic(ServerPlayerEntity targetPlayer, String translationKey, Object... args) {
        targetPlayer.sendMessage(translator.getTextForTarget(targetPlayer.getUuid(), translationKey, args));
    }

    @Override
    public void styled(CommandContext<ServerCommandSource> command, Style style, String translationKey, Object... args) {
        command.getSource().sendFeedback(translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(style), false);
    }

    @Override
    public void styled(ServerPlayerEntity targetPlayer, Style style, String translationKey, Object... args) {
        targetPlayer.sendMessage(translator.getTextForTarget(targetPlayer.getUuid(), translationKey, args).setStyle(style));
    }
}
