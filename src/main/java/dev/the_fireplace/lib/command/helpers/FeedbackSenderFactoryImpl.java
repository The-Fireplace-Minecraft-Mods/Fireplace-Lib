package dev.the_fireplace.lib.command.helpers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Implementation
@Singleton
public final class FeedbackSenderFactoryImpl implements FeedbackSenderFactory
{
	private final Map<Translator, FeedbackSender> feedbackSenders = new ConcurrentHashMap<>();
	private final TextStyles textStyles;
	private final MessageQueue messageQueue;

	@Inject
	public FeedbackSenderFactoryImpl(TextStyles textStyles, MessageQueue messageQueue) {
		this.textStyles = textStyles;
		this.messageQueue = messageQueue;
	}

	@Override
	public FeedbackSender get(Translator translator) {
		return feedbackSenders.computeIfAbsent(translator, computeNewFeedbackSender());
	}

	private Function<Translator, FeedbackSender> computeNewFeedbackSender() {
		return translator -> new SendFeedback(translator, textStyles, messageQueue);
	}
}
