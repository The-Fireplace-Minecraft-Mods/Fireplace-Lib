package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.ServerPacketReceiverRegistry;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import dev.the_fireplace.lib.network.server.ClientConnectedPacketReceiver;
import net.minecraft.util.Identifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class NetworkEvents
{
    public static final Identifier CLIENT_CONNECTED_CHANNEL_NAME = new Identifier(FireplaceLibConstants.MODID, "client_connected");

    private final ServerPacketReceiverRegistry registry;
    private final ClientConnectedPacketReceiver clientConnectedPacketReceiver;
    private final LocalizedClients localizedClients;

    @Inject
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
