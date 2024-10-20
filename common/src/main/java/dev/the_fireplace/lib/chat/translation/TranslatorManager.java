package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
@Singleton
@Implementation
public final class TranslatorManager implements TranslatorFactory
{
    private final ConcurrentMap<String, Translator> modTranslators = new ConcurrentHashMap<>();
    private final EmptyUUID emptyUUID;
    private final LocalizedClients localizedClients;
    private final I18n i18n;

    @Inject
    public TranslatorManager(
        EmptyUUID emptyUUID,
        LocalizedClients localizedClients,
        I18n i18n
    ) {
        this.emptyUUID = emptyUUID;
        this.localizedClients = localizedClients;
        this.i18n = i18n;
    }

    @Override
    public void addTranslator(String modId) {
        modTranslators.put(modId, new TranslatorImpl(modId));
    }

    @Override
    public Translator getTranslator(String modId) {
        return modTranslators.computeIfAbsent(modId, TranslatorImpl::new);
    }

    @Override
    public Collection<String> availableTranslators() {
        return modTranslators.keySet();
    }

    @ThreadSafe
    private class TranslatorImpl implements Translator
    {
        private final String modId;

        private TranslatorImpl(String modId) {
            this.modId = modId;
        }

        @Override
        public MutableComponent getTextForTarget(CommandSourceStack target, String translationKey, Object... arguments) {
            return getTextForTarget(getTargetPlayerId(target), translationKey, arguments);
        }

        @Override
        public MutableComponent getTextForTarget(CommandSource target, String translationKey, Object... arguments) {
            return getTextForTarget(getTargetPlayerId(target), translationKey, arguments);
        }

        @Override
        public MutableComponent getTextForTarget(UUID targetPlayerId, String translationKey, Object... arguments) {
            if (localizedClients.isLocalized(modId, targetPlayerId)) {
                return Component.translatable(translationKey, arguments);
            } else {
                return getTranslatedText(translationKey, arguments);
            }
        }

        @Override
        public MutableComponent getTranslatedText(String translationKey, Object... arguments) {
            Object[] readableTranslationArguments = convertArgumentsToStrings(arguments);

            return Component.literal(i18n.translateToLocalFormatted(modId, translationKey, readableTranslationArguments));
        }

        private Object[] convertArgumentsToStrings(Object[] arguments) {
            Object[] convertedArgs = arguments.clone();

            for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
                if (arguments[argumentIndex] instanceof FormattedText visitable) {
                    convertedArgs[argumentIndex] = visitable.getString();
                }
            }

            return convertedArgs;
        }

        @Override
        public String getTranslatedString(String translationKey, Object... arguments) {
            return getTranslatedText(translationKey, arguments).getString();
        }

        @Override
        public String getTranslationKeyForTarget(CommandSource target, String translationKey) {
            return getTranslationKeyForTarget(getTargetPlayerId(target), translationKey);
        }

        @Override
        public String getTranslationKeyForTarget(UUID targetPlayerId, String translationKey) {
            if (localizedClients.isLocalized(modId, targetPlayerId)) {
                return translationKey;
            } else {
                return i18n.translateToLocalFormatted(modId, translationKey);
            }
        }

        protected UUID getTargetPlayerId(CommandSourceStack commandSource) {
            return commandSource.getEntity() instanceof ServerPlayer
                ? commandSource.getEntity().getUUID()
                : emptyUUID.get();
        }

        protected UUID getTargetPlayerId(CommandSource commandOutput) {
            if (commandOutput instanceof ServerPlayer serverPlayerEntity) {
                return serverPlayerEntity.getUUID();
            }

            return emptyUUID.get();
        }
    }
}
