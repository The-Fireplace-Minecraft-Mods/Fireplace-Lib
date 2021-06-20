package dev.the_fireplace.lib.impl.network;

import dev.the_fireplace.lib.api.network.injectables.ServerPacketReceiverRegistry;
import dev.the_fireplace.lib.impl.FireplaceLib;
import dev.the_fireplace.lib.impl.network.server.ClientConnectedPacketReceiver;
import dev.the_fireplace.lib.impl.translation.LocalizedClients;
import net.minecraft.util.Identifier;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class NetworkEvents {
    public static final Identifier CLIENT_CONNECTED_CHANNEL_NAME = new Identifier(FireplaceLib.MODID, "client_connected");

    private final ServerPacketReceiverRegistry registry;
    private final ClientConnectedPacketReceiver clientConnectedPacketReceiver;
    private final LocalizedClients localizedClients;

    public NetworkEvents(
        ServerPacketReceiverRegistry registry,
        ClientConnectedPacketReceiver clientConnectedPacketReceiver,
        LocalizedClients localizedClients
    ) {
        this.registry = registry;
        this.clientConnectedPacketReceiver = clientConnectedPacketReceiver;
        this.localizedClients = localizedClients;
    }

    public void init() {
        registry.register(clientConnectedPacketReceiver);
    }

    public void onDisconnected(UUID player) {
        localizedClients.removePlayer(player);
    }
}
