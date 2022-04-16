package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import io.netty.util.internal.ConcurrentSet;

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
    private static final Function<String, Set<UUID>> NEW_CONCURRENT_SET = unused -> new ConcurrentSet<>();

    private final Map<String, Set<UUID>> localizedClients = new ConcurrentHashMap<>();
    private final TranslatorFactory translatorFactory;

    @Inject
    public LocalizedClientsImpl(TranslatorFactory translatorFactory) {
        this.translatorFactory = translatorFactory;
    }

    @Override
    public void addPlayer(UUID player, Collection<String> clientModids) {
        for (String modid : getModidsWithTranslators(clientModids)) {
            localizedClients.computeIfAbsent(modid, NEW_CONCURRENT_SET).add(player);
        }
    }

    private Collection<String> getModidsWithTranslators(Collection<String> clientModids) {
        Collection<String> availableTranslators = translatorFactory.availableTranslators();
        return clientModids.stream()
            .filter(availableTranslators::contains)
            .collect(Collectors.toSet());
    }

    @Override
    public void removePlayer(UUID player) {
        for (Set<UUID> uuids : localizedClients.values()) {
            uuids.remove(player);
        }
    }

    @Override
    public boolean isLocalized(String modid, UUID player) {
        return localizedClients.containsKey(modid) && localizedClients.get(modid).contains(player);
    }
}
