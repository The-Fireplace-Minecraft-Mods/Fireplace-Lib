package dev.the_fireplace.lib.impl.translation;

import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.api.chat.TranslatorManager;
import dev.the_fireplace.lib.api.util.EmptyUUID;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public final class TranslatorManagerImpl implements TranslatorManager {
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

    @ThreadSafe
    private static final class TranslatorImpl implements Translator {
        private final String modid;

        private TranslatorImpl(String modid) {
            this.modid = modid;
        }

        @Override
        public Text getTextForTarget(ServerCommandSource target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public Text getTextForTarget(CommandOutput target, String translationKey, Object... args) {
            return getTextForTarget(getTargetId(target), translationKey, args);
        }

        @Override
        public Text getTextForTarget(UUID target, String translationKey, Object... args) {
            if (!LocalizedClients.isLocalized(modid, target)) {
                return getTranslatedText(translationKey, args);
            } else {
                return new TranslatableText(translationKey, args);
            }
        }

        @Override
        public LiteralText getTranslatedText(String translationKey, Object... args) {
            Object[] convertedArgs = getArgsWithTextConvertedToString(args);

            return new LiteralText(I18n.translateToLocalFormatted(modid, translationKey, convertedArgs));
        }

        private Object[] getArgsWithTextConvertedToString(Object[] args) {
            Object[] convertedArgs = args.clone();

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Text) {
                    convertedArgs[i] = ((Text) args[i]).getString();
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
            return commandOutput instanceof ServerPlayerEntity ? ((Entity) commandOutput).getUuid() : EmptyUUID.EMPTY_UUID;
        }
    }
}