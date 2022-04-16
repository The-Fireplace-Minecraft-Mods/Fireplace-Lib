package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;

public interface ClientboundReceiverRegistry
{
    void registerReceiver(ClientboundPacketSpecification specification);
}
