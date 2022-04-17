package dev.the_fireplace.lib.domain.network;

import java.util.UUID;

public interface NetworkEvents
{
    void onDisconnected(UUID player);
}
