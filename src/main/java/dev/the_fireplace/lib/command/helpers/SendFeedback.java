package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;

public final class SendFeedback implements FeedbackSender {
    private final Translator translator;
    private final TextStyles textStyles;

    SendFeedback(Translator translator, TextStyles textStyles) {
        this.translator = translator;
        this.textStyles = textStyles;
    }

    @Override
    public int throwFailure(CommandContext<ServerCommandSource> command, String translationKey, Object... args) throws CommandException {
        throw new CommandException(translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(textStyles.red()));
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
