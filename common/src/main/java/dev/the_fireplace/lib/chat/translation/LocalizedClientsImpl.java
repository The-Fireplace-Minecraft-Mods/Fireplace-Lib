package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Implementation
@Singleton
public final class LocalizedClientsImpl implements LocalizedClients
{
    private static final Function<String, Set<UUID>> NEW_CONCURRENT_SET = unused -> ConcurrentHashMap.newKeySet();

    private final Map<String, Set<UUID>> localizedPlayersByModId = new ConcurrentHashMap<>();
    private final TranslatorFactory translatorFactory;

    @Inject
    public LocalizedClientsImpl(TranslatorFactory translatorFactory) {
        this.translatorFactory = translatorFactory;
    }

    @Override
    public void addPlayer(UUID playerId, Collection<String> clientModIds) {
        for (String modId : getModIdsWithTranslators(clientModIds)) {
            localizedPlayersByModId.computeIfAbsent(modId, NEW_CONCURRENT_SET).add(playerId);
        }
    }

    private Collection<String> getModIdsWithTranslators(Collection<String> clientModIds) {
        Collection<String> availableTranslators = translatorFactory.availableTranslators();
        return clientModIds.stream()
            .filter(availableTranslators::contains)
            .collect(Collectors.toSet());
    }

    @Override
    public void removePlayer(UUID playerId) {
        for (Set<UUID> localizedPlayers : localizedPlayersByModId.values()) {
            localizedPlayers.remove(playerId);
        }
    }

    @Override
    public boolean isLocalized(String modId, UUID playerId) {
        return localizedPlayersByModId.containsKey(modId) && localizedPlayersByModId.get(modId).contains(playerId);
    }
}
