package dev.the_fireplace.lib.api.command;

import dev.the_fireplace.lib.api.chat.internal.Translator;

public interface FeedbackSenderFactory {
	FeedbackSender get(Translator translator);
}
