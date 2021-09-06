package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.mixin.ServerCommandSourceAccessor;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;

public final class SendFeedback implements FeedbackSender {
    private final Translator translator;
    private final TextStyles textStyles;
    private final MessageQueue messageQueue;

    SendFeedback(Translator translator, TextStyles textStyles, MessageQueue messageQueue) {
        this.translator = translator;
        this.textStyles = textStyles;
        this.messageQueue = messageQueue;
    }

    @Override
    public int throwFailure(CommandContext<ServerCommandSource> command, String translationKey, Object... args) throws CommandException {
        throw new CommandException(translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(textStyles.red()));
    }

    @Override
    public void basic(CommandContext<ServerCommandSource> command, String translationKey, Object... args) {
        ServerCommandSourceAccessor serverCommandSource = (ServerCommandSourceAccessor) command.getSource();
        messageQueue.queueMessages(serverCommandSource.getOutput(), translator.getTextForTarget(command.getSource(), translationKey, args));
    }

    @Override
    public void basic(ServerPlayerEntity targetPlayer, String translationKey, Object... args) {
        messageQueue.queueMessages(targetPlayer, translator.getTextForTarget(targetPlayer.getUuid(), translationKey, args));
    }

    @Override
    public void styled(CommandContext<ServerCommandSource> command, Style style, String translationKey, Object... args) {
        ServerCommandSourceAccessor serverCommandSource = (ServerCommandSourceAccessor) command.getSource();
        messageQueue.queueMessages(serverCommandSource.getOutput(), translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(style));
    }

    @Override
    public void styled(ServerPlayerEntity targetPlayer, Style style, String translationKey, Object... args) {
        messageQueue.queueMessages(targetPlayer, translator.getTextForTarget(targetPlayer.getUuid(), translationKey, args).setStyle(style));
    }
}
