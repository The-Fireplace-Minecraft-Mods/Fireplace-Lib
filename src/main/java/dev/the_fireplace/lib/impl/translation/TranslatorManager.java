package dev.the_fireplace.lib.impl.translation;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.uuid.lib.EmptyUUID;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
@Singleton
@Implementation
public final class TranslatorManager implements TranslatorFactory {
    private final Map<String, Translator> TRANSLATION_SERVICES = new ConcurrentHashMap<>();

    @Override
    public void addTranslator(String modid) {
        TRANSLATION_SERVICES.put(modid, new TranslatorImpl(modid));
    }

    @Override
    public Translator getTranslator(String modid) {
        return TRANSLATION_SERVICES.computeIfAbsent(modid, TranslatorImpl::new);
    }

    @Override
    public Collection<String> availableTranslators() {
        return TRANSLATION_SERVICES.keySet();
    }

    @ThreadSafe
    private static class TranslatorImpl implements Translator {
        private final String modid;
        private final LocalizedClients localizedClients;
        private final I18n i18n;

        private TranslatorImpl(String modid) {
            this.modid = modid;
            this.localizedClients = DIContainer.get().getInstance(LocalizedClients.class);
            this.i18n = DIContainer.get().getInstance(I18n.class);
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
        public LiteralText getTranslatedText(String translationKey, Object... args) {
            Object[] convertedArgs = getArgsWithTextConvertedToString(args);

            return new LiteralText(i18n.translateToLocalFormatted(modid, translationKey, convertedArgs));
        }

        private Object[] getArgsWithTextConvertedToString(Object[] args) {
            Object[] convertedArgs = args.clone();

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof StringVisitable) {
                    convertedArgs[i] = ((StringVisitable) args[i]).getString();
                }
            }

            return convertedArgs;
        }

        @Override
        public String getTranslatedString(String translationKey, Object... args) {
            return getTranslatedText(translationKey, args).getString();
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
            return commandSource.getEntity() instanceof ServerPlayerEntity ? commandSource.getEntity().getUuid() : EmptyUUID.EMPTY_UUID;
        }

        protected UUID getTargetId(CommandOutput commandOutput) {
            return commandOutput instanceof ServerPlayerEntity ? ((Entity) commandOutput).getUuid() : EmptyUUID.EMPTY_UUID;
        }
    }
}
