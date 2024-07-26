package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.domain.network.NetworkRegister;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;

import javax.inject.Inject;
import java.util.UUID;

public final class NetworkEvents
{
    private final PacketSpecificationRegistry registry;
    private final ServerboundPackets serverboundPackets;
    private final LocalizedClients localizedClients;
    private final NetworkRegister networkRegister;

    @Inject
    public NetworkEvents(
        PacketSpecificationRegistry registry,
        ServerboundPackets serverboundPackets,
        LocalizedClients localizedClients,
        NetworkRegister networkRegister
    ) {
        this.registry = registry;
        this.serverboundPackets = serverboundPackets;
        this.localizedClients = localizedClients;
        this.networkRegister = networkRegister;
    }

    public void init() {
        networkRegister.register();
        registry.register(serverboundPackets.clientConnected());
    }

    public void onDisconnected(UUID player) {
        localizedClients.removePlayer(player);
    }
}
