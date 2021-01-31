package the_fireplace.lib.impl.command;

import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.command.FeedbackSender;
import the_fireplace.lib.api.command.FeedbackSenderManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FeedbackSenderManagerImpl implements FeedbackSenderManager {
	@Deprecated
	public static final FeedbackSenderManager INSTANCE = new FeedbackSenderManagerImpl();

	private FeedbackSenderManagerImpl(){}

	private final Map<Translator, FeedbackSender> feedbackSenders = new ConcurrentHashMap<>();

	@Override
	public FeedbackSender get(Translator translator) {
		return feedbackSenders.computeIfAbsent(translator, SendFeedback::new);
	}
}
