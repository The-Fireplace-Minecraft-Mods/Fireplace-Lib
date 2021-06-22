package dev.the_fireplace.lib.network.server;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.ServerPacketReceiverRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ServerPacketReceiver;
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
