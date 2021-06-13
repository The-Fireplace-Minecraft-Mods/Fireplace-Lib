package dev.the_fireplace.lib.impl.translation;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.chat.TranslatorFactory;
import io.netty.util.internal.ConcurrentSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Implementation
@Singleton
public final class LocalizedClientsImpl implements LocalizedClients {
    private final Map<String, Set<UUID>> LOCALIZED_CLIENTS = new ConcurrentHashMap<>();
    private final TranslatorFactory translatorFactory;

    @Inject
    private LocalizedClientsImpl(TranslatorFactory translatorFactory) {
        this.translatorFactory = translatorFactory;
    }

    @Override
    public void addPlayer(UUID player, Collection<String> clientModids) {
        for (String modid: clientModids.stream().filter(translatorFactory.availableTranslators()::contains).collect(Collectors.toSet())) {
            LOCALIZED_CLIENTS.computeIfAbsent(modid, u -> new ConcurrentSet<>()).add(player);
        }
    }

    @Override
    public void removePlayer(UUID player) {
        for (Set<UUID> uuids : LOCALIZED_CLIENTS.values()) {
            uuids.remove(player);
        }
    }

    @Override
    public boolean isLocalized(String modid, UUID player) {
        return LOCALIZED_CLIENTS.containsKey(modid) && LOCALIZED_CLIENTS.get(modid).contains(player);
    }
}
