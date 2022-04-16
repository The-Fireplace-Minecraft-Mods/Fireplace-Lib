package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import javax.inject.Inject;
import javax.inject.Singleton;

@Implementation
@Singleton
public final class FabricPacketSpecificationRegistry implements PacketSpecificationRegistry
{
    private final ClientboundReceiverRegistry clientboundReceiverRegistry;

    @Inject
    public FabricPacketSpecificationRegistry(ClientboundReceiverRegistry clientboundReceiverRegistry) {
        this.clientboundReceiverRegistry = clientboundReceiverRegistry;
    }

    @Override
    public void register(ServerboundPacketSpecification specification) {
        ServerPlayNetworking.registerGlobalReceiver(
            specification.getPacketID(),
            (server, player, handler, buf, responseSender) -> specification.getReceiverFactory().get().receive(server, player, handler, buf)
        );
    }

    @Override
    public void register(ClientboundPacketSpecification specification) {
        this.clientboundReceiverRegistry.registerReceiver(specification);
    }
}
