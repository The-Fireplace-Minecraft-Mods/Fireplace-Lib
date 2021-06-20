package dev.the_fireplace.lib.impl.domain.translation;

import java.util.Collection;
import java.util.UUID;

public interface LocalizedClients {
    void addPlayer(UUID player, Collection<String> clientModids);

    void removePlayer(UUID player);

    boolean isLocalized(String modid, UUID player);
}
