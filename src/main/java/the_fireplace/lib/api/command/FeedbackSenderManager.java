package the_fireplace.lib.api.command;

import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.impl.command.FeedbackSenderManagerImpl;

public interface FeedbackSenderManager {
	static FeedbackSenderManager getInstance() {
		//noinspection deprecation
		return FeedbackSenderManagerImpl.INSTANCE;
	}

	FeedbackSender get(Translator translator);
}
