package dev.the_fireplace.lib.impl.network.client;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.network.client.ClientPacketReceiver;
import dev.the_fireplace.lib.api.network.client.ClientPacketReceiverRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import javax.inject.Singleton;

@Environment(EnvType.CLIENT)
@Implementation
@Singleton
public final class ClientPacketReceiverRegistryImpl implements ClientPacketReceiverRegistry {

    @Override
    public void register(ClientPacketReceiver receiver) {
        ClientPlayNetworking.registerGlobalReceiver(receiver.getId(), receiver);
    }
}
