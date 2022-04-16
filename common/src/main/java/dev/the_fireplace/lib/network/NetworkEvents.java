package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class NetworkEvents
{
    private final PacketSpecificationRegistry registry;
    private final ServerboundPackets serverboundPackets;
    private final LocalizedClients localizedClients;

    @Inject
    public NetworkEvents(
        PacketSpecificationRegistry registry,
        ServerboundPackets serverboundPackets,
        LocalizedClients localizedClients
    ) {
        this.registry = registry;
        this.serverboundPackets = serverboundPackets;
        this.localizedClients = localizedClients;
    }

    public void init() {
        registry.register(serverboundPackets.clientConnected());
    }

    public void onDisconnected(UUID player) {
        localizedClients.removePlayer(player);
    }
}
