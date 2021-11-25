package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

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
        public MutableText getTextForTarget(ServerCommandSource target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public MutableText getTextForTarget(CommandOutput target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public MutableText getTextForTarget(UUID target, String translationKey, Object... args) {
            if (!localizedClients.isLocalized(modid, target)) {
                return getTranslatedText(translationKey, args);
            } else {
                return new TranslatableText(translationKey, args);
            }
        }

        @Override
        public LiteralText getTranslatedText(String translationKey, Object... translationArguments) {
            Object[] readableTranslationArguments = convertArgumentsToStrings(translationArguments);

            return new LiteralText(i18n.translateToLocalFormatted(modid, translationKey, readableTranslationArguments));
        }

        private Object[] convertArgumentsToStrings(Object[] arguments) {
            Object[] convertedArgs = arguments.clone();

            for (int argumentIndex = 0; argumentIndex < arguments.length; argumentIndex++) {
                if (arguments[argumentIndex] instanceof StringVisitable) {
                    convertedArgs[argumentIndex] = ((StringVisitable) arguments[argumentIndex]).getString();
                }
            }

            return convertedArgs;
        }

        @Override
        public String getTranslatedString(String translationKey, Object... translationArguments) {
            return getTranslatedText(translationKey, translationArguments).getString();
        }

        @Override
        public String getTranslationKeyForTarget(CommandOutput target, String translationKey) {
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

        protected UUID getTargetId(ServerCommandSource commandSource) {
            return commandSource.getEntity() instanceof ServerPlayerEntity
                ? commandSource.getEntity().getUuid()
                : emptyUUID.get();
        }

        protected UUID getTargetId(CommandOutput commandOutput) {
            return commandOutput instanceof ServerPlayerEntity ? ((Entity) commandOutput).getUuid() : emptyUUID.get();
        }
    }
}
