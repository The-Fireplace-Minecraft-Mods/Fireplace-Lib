package dev.the_fireplace.lib.api.command;

import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.impl.command.FeedbackSenderManagerImpl;

public interface FeedbackSenderManager {
	static FeedbackSenderManager getInstance() {
		//noinspection deprecation
		return FeedbackSenderManagerImpl.INSTANCE;
	}

	FeedbackSender get(Translator translator);
}
