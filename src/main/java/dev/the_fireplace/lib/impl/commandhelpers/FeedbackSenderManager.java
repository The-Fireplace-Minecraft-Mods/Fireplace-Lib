package dev.the_fireplace.lib.impl.commandhelpers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class FeedbackSenderManager implements FeedbackSenderFactory {
	private final Map<Translator, FeedbackSender> feedbackSenders = new ConcurrentHashMap<>();
	private final TextStyles textStyles;

	@Inject
	public FeedbackSenderManager(TextStyles textStyles) {
		this.textStyles = textStyles;
	}

	@Override
	public FeedbackSender get(Translator translator) {
		return feedbackSenders.computeIfAbsent(translator, t -> new SendFeedback(t, textStyles));
	}
}
