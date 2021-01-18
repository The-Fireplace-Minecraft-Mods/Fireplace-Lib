package the_fireplace.lib.impl.translation;

import io.netty.util.internal.ConcurrentSet;
import the_fireplace.lib.api.chat.TranslatorManager;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class LocalizedClients {
    private static final Map<String, Set<UUID>> LOCALIZED_CLIENTS = new ConcurrentHashMap<>();
    private static final TranslatorManager translatorManager = TranslatorManager.getInstance();

    public static void addPlayer(UUID player, Collection<String> clientModids) {
        for (String modid: clientModids.stream().filter(translatorManager.availableTranslators()::contains).collect(Collectors.toSet())) {
            LOCALIZED_CLIENTS.computeIfAbsent(modid, u -> new ConcurrentSet<>()).add(player);
        }
    }

    public static void removePlayer(UUID player) {
        for (String modid: LOCALIZED_CLIENTS.keySet()) {
            LOCALIZED_CLIENTS.get(modid).remove(player);
        }
    }

    public static boolean isLocalized(String modid, UUID player) {
        return !LOCALIZED_CLIENTS.containsKey(modid) || !LOCALIZED_CLIENTS.get(modid).contains(player);
    }
}
