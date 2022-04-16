package dev.the_fireplace.lib.api.network.interfaces;

import java.util.function.Supplier;

/**
 * Full specification for a serverbound packet. DO NOT classload any client side classes within the specification class.
 */
public interface ServerboundPacketSpecification extends PacketSpecification
{
    Supplier<ServerboundPacketReceiver> getReceiverFactory();
}
