package the_fireplace.lib.api.chat;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import the_fireplace.lib.api.util.EmptyUUID;
import the_fireplace.lib.impl.translation.I18n;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TranslationService {
    private static final Map<String, TranslationService> TRANSLATION_SERVICES = new ConcurrentHashMap<>();

    /**
     * Properly initialize your translation service.
     * This should happen on both client (If the mod is present there) and server to ensure proper synchronization.
     * {@link net.fabricmc.api.ModInitializer#onInitialize} is a good place to do so.
     */
    public static void initialize(String modid) {
        TRANSLATION_SERVICES.put(modid, new TranslationService(modid));
    }

    public static TranslationService get(String modid) {
        return TRANSLATION_SERVICES.computeIfAbsent(modid, TranslationService::new);
    }

    public static Collection<String> availableTranslationServices() {
        return TRANSLATION_SERVICES.keySet();
    }

    public static void addPlayer(UUID player, Collection<String> clientModids) {
        for (String modid: clientModids.stream().filter(TRANSLATION_SERVICES::containsKey).collect(Collectors.toSet())) {
            get(modid).clientsWithLocalizations.add(player);
        }
    }

    public static void removePlayer(UUID player) {
        for (String modid: availableTranslationServices()) {
            get(modid).clientsWithLocalizations.remove(player);
        }
    }

    private final Set<UUID> clientsWithLocalizations = new ConcurrentSet<>();
    private final String modid;

    private TranslationService(String modid) {
        this.modid = modid;
    }

    public Text getTextForTarget(ServerCommandSource target, String translationKey, Object... args) {
        return getTextForTarget(getTargetId(target), translationKey, args);
    }

    public Text getTextForTarget(CommandOutput target, String translationKey, Object... args) {
        return getTextForTarget(getTargetId(target), translationKey, args);
    }

    public Text getTextForTarget(UUID target, String translationKey, Object... args) {
        if (!clientsWithLocalizations.contains(target)) {
            return getTranslatedText(translationKey, args);
        } else {
            return new TranslatableText(translationKey, args);
        }
    }

    public LiteralText getTranslatedText(String translationKey, Object... args) {
        return new LiteralText(I18n.translateToLocalFormatted(modid, translationKey, args));
    }

    public String getTranslatedString(String translationKey, Object... args) {
        return getTranslatedText(translationKey, args).getString();
    }

    public String getTranslationKeyForTarget(CommandOutput target, String translationKey) {
        return getTranslationKeyForTarget(getTargetId(target), translationKey);
    }

    public String getTranslationKeyForTarget(UUID target, String translationKey) {
        if (!clientsWithLocalizations.contains(target)) {
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
