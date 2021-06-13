package dev.the_fireplace.lib.api.network.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientPacketReceiverRegistry {
    void register(ClientPacketReceiver receiver);
}
