package dev.the_fireplace.lib.network.client;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.ClientPacketReceiverRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ClientPacketReceiver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import javax.inject.Singleton;

@Implementation(environment = "CLIENT")
@Singleton
public final class ClientPacketReceiverRegistryImpl implements ClientPacketReceiverRegistry
{

    @Override
    public void register(ClientPacketReceiver receiver) {
        ClientPlayNetworking.registerGlobalReceiver(receiver.getId(), receiver);
    }
}
