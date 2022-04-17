package dev.the_fireplace.lib.api.network.injectables;

import dev.the_fireplace.lib.api.network.interfaces.ServerPacketReceiver;

public interface ServerPacketReceiverRegistry
{
    void register(ServerPacketReceiver receiver);
}
