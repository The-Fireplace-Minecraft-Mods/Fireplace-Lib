package dev.the_fireplace.lib.api.network.client;

import dev.the_fireplace.lib.impl.network.client.ClientPacketReceiverRegistryImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientPacketReceiverRegistry {
    static ClientPacketReceiverRegistry getInstance() {
        //noinspection deprecation
        return ClientPacketReceiverRegistryImpl.INSTANCE;
    }
    void register(ClientPacketReceiver receiver);
}
