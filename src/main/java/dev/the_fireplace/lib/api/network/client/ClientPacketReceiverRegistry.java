package dev.the_fireplace.lib.api.network.client;

import dev.the_fireplace.lib.impl.network.client.ClientPacketReceiverRegistryImpl;

public interface ClientPacketReceiverRegistry {
    static ClientPacketReceiverRegistry getInstance() {
        //noinspection deprecation
        return ClientPacketReceiverRegistryImpl.INSTANCE;
    }
    void register(ClientPacketReceiver receiver);
}
