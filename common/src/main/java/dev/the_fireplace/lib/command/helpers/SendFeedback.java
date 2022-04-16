package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.mixin.CommandSourceStackAccessor;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public final class SendFeedback implements FeedbackSender
{
    private final Translator translator;
    private final TextStyles textStyles;
    private final MessageQueue messageQueue;

    SendFeedback(Translator translator, TextStyles textStyles, MessageQueue messageQueue) {
        this.translator = translator;
        this.textStyles = textStyles;
        this.messageQueue = messageQueue;
    }

    @Override
    public int throwFailure(CommandContext<CommandSourceStack> command, String translationKey, Object... args) throws CommandRuntimeException {
        throw new CommandRuntimeException(translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(textStyles.red()));
    }

    @Override
    public void basic(CommandContext<CommandSourceStack> command, String translationKey, Object... args) {
        CommandSourceStackAccessor serverCommandSource = (CommandSourceStackAccessor) command.getSource();
        messageQueue.queueMessages(serverCommandSource.getSource(), translator.getTextForTarget(command.getSource(), translationKey, args));
    }

    @Override
    public void basic(ServerPlayer targetPlayer, String translationKey, Object... args) {
        messageQueue.queueMessages(targetPlayer, translator.getTextForTarget(targetPlayer.getUUID(), translationKey, args));
    }

    @Override
    public void styled(CommandContext<CommandSourceStack> command, Style style, String translationKey, Object... args) {
        CommandSourceStackAccessor serverCommandSource = (CommandSourceStackAccessor) command.getSource();
        messageQueue.queueMessages(serverCommandSource.getSource(), translator.getTextForTarget(command.getSource(), translationKey, args).setStyle(style));
    }

    @Override
    public void styled(ServerPlayer targetPlayer, Style style, String translationKey, Object... args) {
        messageQueue.queueMessages(targetPlayer, translator.getTextForTarget(targetPlayer.getUUID(), translationKey, args).setStyle(style));
    }
}
