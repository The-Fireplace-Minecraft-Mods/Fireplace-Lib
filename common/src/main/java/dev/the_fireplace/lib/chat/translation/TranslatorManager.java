package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

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
        public Component getTextForTarget(CommandSourceStack target, String translationKey, Object... arguments) {
            return getTextForTarget(getTargetPlayerId(target), translationKey, arguments);
        }

        @Override
        public Component getTextForTarget(CommandSource target, String translationKey, Object... arguments) {
            return getTextForTarget(getTargetPlayerId(target), translationKey, arguments);
        }

        @Override
        public Component getTextForTarget(UUID targetPlayerId, String translationKey, Object... arguments) {
            if (localizedClients.isLocalized(modId, targetPlayerId)) {
                return new TranslatableComponent(translationKey, arguments);
            } else {
                return getTranslatedText(translationKey, arguments);
            }
        }

        @Override
        public TextComponent getTranslatedText(String translationKey, Object... arguments) {
            Object[] readableTranslationArguments = convertArgumentsToStrings(arguments);

            return new TextComponent(i18n.translateToLocalFormatted(modId, translationKey, readableTranslationArguments));
        }

        private Object[] convertArgumentsToStrings(Object[] arguments) {
            Object[] convertedArgs = arguments.clone();

            for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
                if (arguments[argumentIndex] instanceof Component) {
                    convertedArgs[argumentIndex] = ((Component) arguments[argumentIndex]).getString();
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
            return commandOutput instanceof ServerPlayer ? ((Entity) commandOutput).getUUID() : emptyUUID.get();
        }
    }
}
