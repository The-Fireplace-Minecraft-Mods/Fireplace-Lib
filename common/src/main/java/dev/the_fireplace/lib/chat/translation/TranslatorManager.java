package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
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
    public void addTranslator(String modid) {
        modTranslators.put(modid, new TranslatorImpl(modid));
    }

    @Override
    public Translator getTranslator(String modid) {
        return modTranslators.computeIfAbsent(modid, TranslatorImpl::new);
    }

    @Override
    public Collection<String> availableTranslators() {
        return modTranslators.keySet();
    }

    @ThreadSafe
    private class TranslatorImpl implements Translator
    {
        private final String modid;

        private TranslatorImpl(String modid) {
            this.modid = modid;
        }

        @Override
        public MutableComponent getTextForTarget(CommandSourceStack target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public MutableComponent getTextForTarget(CommandSource target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public MutableComponent getTextForTarget(UUID target, String translationKey, Object... args) {
            if (!localizedClients.isLocalized(modid, target)) {
                return getTranslatedText(translationKey, args);
            } else {
                return new TranslatableComponent(translationKey, args);
            }
        }

        @Override
        public TextComponent getTranslatedText(String translationKey, Object... translationArguments) {
            Object[] readableTranslationArguments = convertArgumentsToStrings(translationArguments);

            return new TextComponent(i18n.translateToLocalFormatted(modid, translationKey, readableTranslationArguments));
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
        public String getTranslatedString(String translationKey, Object... translationArguments) {
            return getTranslatedText(translationKey, translationArguments).getString();
        }

        @Override
        public String getTranslationKeyForTarget(CommandSource target, String translationKey) {
            return getTranslationKeyForTarget(getTargetId(target), translationKey);
        }

        @Override
        public String getTranslationKeyForTarget(UUID target, String translationKey) {
            if (!localizedClients.isLocalized(modid, target)) {
                return i18n.translateToLocalFormatted(modid, translationKey);
            } else {
                return translationKey;
            }
        }

        protected UUID getTargetId(CommandSourceStack commandSource) {
            return commandSource.getEntity() instanceof ServerPlayer
                ? commandSource.getEntity().getUUID()
                : emptyUUID.get();
        }

        protected UUID getTargetId(CommandSource commandOutput) {
            return commandOutput instanceof ServerPlayer ? ((Entity) commandOutput).getUUID() : emptyUUID.get();
        }
    }
}
