package dev.the_fireplace.lib.impl.commandhelpers;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.chat.internal.Translator;
import dev.the_fireplace.lib.api.command.FeedbackSender;
import dev.the_fireplace.lib.api.command.FeedbackSenderFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class FeedbackSenderManager implements FeedbackSenderFactory {
	private final Map<Translator, FeedbackSender> feedbackSenders = new ConcurrentHashMap<>();

	@Override
	public FeedbackSender get(Translator translator) {
		return feedbackSenders.computeIfAbsent(translator, SendFeedback::new);
	}
}
