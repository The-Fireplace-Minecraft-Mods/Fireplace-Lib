package dev.the_fireplace.lib.api.network.injectables;

import dev.the_fireplace.lib.api.network.interfaces.ClientPacketReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientPacketReceiverRegistry
{
    void register(ClientPacketReceiver receiver);
}
