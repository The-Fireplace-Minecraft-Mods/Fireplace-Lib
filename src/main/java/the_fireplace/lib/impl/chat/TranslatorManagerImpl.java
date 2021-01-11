package the_fireplace.lib.impl.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import the_fireplace.lib.api.chat.Translator;
import the_fireplace.lib.api.chat.TranslatorManager;
import the_fireplace.lib.api.util.EmptyUUID;
import the_fireplace.lib.impl.translation.I18n;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TranslatorManagerImpl implements TranslatorManager {
    @Deprecated
    public static final TranslatorManager INSTANCE = new TranslatorManagerImpl();
    private static final Map<String, Translator> TRANSLATION_SERVICES = new ConcurrentHashMap<>();

    private TranslatorManagerImpl(){}

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

    private static class TranslatorImpl implements Translator {
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
            if (!LocalizedClients.isLocalized(modid, target)) {
                return getTranslatedText(translationKey, args);
            } else {
                return new TranslatableText(translationKey, args);
            }
        }

        @Override
        public LiteralText getTranslatedText(String translationKey, Object... args) {
            return new LiteralText(I18n.translateToLocalFormatted(modid, translationKey, args));
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
            if (!LocalizedClients.isLocalized(modid, target)) {
                return I18n.translateToLocalFormatted(modid, translationKey);
            } else {
                return translationKey;
            }
        }

        protected UUID getTargetId(ServerCommandSource commandSource) {
            return commandSource.getEntity() instanceof ServerPlayerEntity ? commandSource.getEntity().getUuid() : EmptyUUID.EMPTY_UUID;
        }

        protected UUID getTargetId(CommandOutput commandOutput) {
            return commandOutput instanceof ServerPlayerEntity ? ((ServerPlayerEntity) commandOutput).getUuid() : EmptyUUID.EMPTY_UUID;
        }
    }
}
