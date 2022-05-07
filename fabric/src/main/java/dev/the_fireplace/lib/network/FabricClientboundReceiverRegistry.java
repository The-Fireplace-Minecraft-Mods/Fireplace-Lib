package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Implementation(environment = "CLIENT")
public final class FabricClientboundReceiverRegistry implements ClientboundReceiverRegistry
{
    @Override
    public void registerReceiver(ClientboundPacketSpecification specification) {
        ClientPlayNetworking.registerGlobalReceiver(
            specification.getPacketID(),
            (client, handler, buf, responseSender) -> specification.getReceiverFactory().get().receive(client, handler, buf)
        );
    }
}
