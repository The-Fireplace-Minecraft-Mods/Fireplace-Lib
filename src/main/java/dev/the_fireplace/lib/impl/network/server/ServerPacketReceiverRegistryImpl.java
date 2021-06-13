package dev.the_fireplace.lib.impl.network.server;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.network.server.ServerPacketReceiver;
import dev.the_fireplace.lib.api.network.server.ServerPacketReceiverRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class ServerPacketReceiverRegistryImpl implements ServerPacketReceiverRegistry {

    @Override
    public void register(ServerPacketReceiver receiver) {
        ServerPlayNetworking.registerGlobalReceiver(receiver.getId(), receiver);
    }
}
