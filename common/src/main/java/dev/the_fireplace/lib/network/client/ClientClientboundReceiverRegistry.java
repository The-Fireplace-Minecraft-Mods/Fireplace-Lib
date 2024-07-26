package dev.the_fireplace.lib.network.client;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import jakarta.inject.Inject;

@Implementation(environment = "CLIENT")
public final class ClientClientboundReceiverRegistry implements ClientboundReceiverRegistry
{
    private final PacketSpecificationRegistry packetSpecificationRegistry;

    @Inject
    public ClientClientboundReceiverRegistry(PacketSpecificationRegistry packetSpecificationRegistry) {
        this.packetSpecificationRegistry = packetSpecificationRegistry;
    }

    @Override
    public void registerReceiver(ClientboundPacketSpecification specification) {
        this.packetSpecificationRegistry.register(specification);
    }
}
