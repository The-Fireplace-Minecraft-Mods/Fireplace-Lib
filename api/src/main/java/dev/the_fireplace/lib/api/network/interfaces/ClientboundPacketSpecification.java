package dev.the_fireplace.lib.api.network.interfaces;

import java.util.function.Supplier;

/**
 * Full specification for a clientbound packet. DO NOT classload any client side classes within the specification class.
 */
public interface ClientboundPacketSpecification extends PacketSpecification
{
    Supplier<ClientboundPacketReceiver> getReceiverFactory();
}
