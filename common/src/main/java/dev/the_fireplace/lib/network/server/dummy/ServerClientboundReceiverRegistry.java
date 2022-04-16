package dev.the_fireplace.lib.network.server.dummy;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;

@Implementation(environment = "SERVER")
public final class ServerClientboundReceiverRegistry implements ClientboundReceiverRegistry
{
    @Override
    public void registerReceiver(ClientboundPacketSpecification specification) {

    }
}
