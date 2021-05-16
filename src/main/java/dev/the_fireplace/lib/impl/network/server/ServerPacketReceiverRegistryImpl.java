package dev.the_fireplace.lib.impl.network.server;

import dev.the_fireplace.lib.api.network.server.ServerPacketReceiver;
import dev.the_fireplace.lib.api.network.server.ServerPacketReceiverRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ServerPacketReceiverRegistryImpl implements ServerPacketReceiverRegistry {
    @Deprecated
    public static final ServerPacketReceiverRegistry INSTANCE = new ServerPacketReceiverRegistryImpl();

    private ServerPacketReceiverRegistryImpl() {}

    @Override
    public void register(ServerPacketReceiver receiver) {
        ServerPlayNetworking.registerGlobalReceiver(receiver.getId(), receiver);
    }
}
