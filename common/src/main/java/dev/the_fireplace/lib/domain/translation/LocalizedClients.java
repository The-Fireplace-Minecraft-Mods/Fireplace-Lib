package dev.the_fireplace.lib.domain.translation;

import java.util.Collection;
import java.util.UUID;

public interface LocalizedClients
{
    void addPlayer(UUID playerId, Collection<String> clientModIds);

    void removePlayer(UUID playerId);

    boolean isLocalized(String modId, UUID playerId);
}
