package dev.the_fireplace.lib.api.network.server;

import dev.the_fireplace.lib.impl.network.server.ServerPacketReceiverRegistryImpl;

public interface ServerPacketReceiverRegistry {
    static ServerPacketReceiverRegistry getInstance() {
        //noinspection deprecation
        return ServerPacketReceiverRegistryImpl.INSTANCE;
    }
    void register(ServerPacketReceiver receiver);
}
