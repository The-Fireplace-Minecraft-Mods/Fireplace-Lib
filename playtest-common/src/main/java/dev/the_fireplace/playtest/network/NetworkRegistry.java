package dev.the_fireplace.playtest.network;

import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import jakarta.inject.Inject;

public final class NetworkRegistry
{
    private final PacketSpecificationRegistry registry;
    private final ClientboundPackets clientboundPackets;
    private final ServerboundPackets serverboundPackets;

    @Inject
    public NetworkRegistry(
        PacketSpecificationRegistry registry,
        ClientboundPackets clientboundPackets,
        ServerboundPackets serverboundPackets
    ) {
        this.registry = registry;
        this.clientboundPackets = clientboundPackets;
        this.serverboundPackets = serverboundPackets;
    }

    public void registerPackets() {
        registry.register(clientboundPackets.getPingResponseSpec());
        registry.register(clientboundPackets.getSecondPingResponseSpec());
        registry.register(serverboundPackets.getSecondPingSpec());
        registry.register(serverboundPackets.getThirdPingSpec());
    }
}
