package dev.the_fireplace.lib.api.network.injectables;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;

public interface PacketSpecificationRegistry
{
    void register(ServerboundPacketSpecification specification);

    void register(ClientboundPacketSpecification specification);
}
