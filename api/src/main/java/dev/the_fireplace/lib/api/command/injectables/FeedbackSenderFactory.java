package dev.the_fireplace.lib.api.command.injectables;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;

public interface FeedbackSenderFactory
{
    FeedbackSender get(Translator translator);
}
