package dev.the_fireplace.lib.api.network.server;

public interface ServerPacketReceiverRegistry {
    void register(ServerPacketReceiver receiver);
}
