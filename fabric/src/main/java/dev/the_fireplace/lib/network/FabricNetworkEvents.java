package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.ServerPacketReceiverRegistry;
import dev.the_fireplace.lib.domain.network.NetworkEvents;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import dev.the_fireplace.lib.network.server.ClientConnectedPacketReceiver;
import net.minecraft.resources.ResourceLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Implementation
public final class FabricNetworkEvents implements NetworkEvents
{
    public static final ResourceLocation CLIENT_CONNECTED_CHANNEL_NAME = new ResourceLocation(FireplaceLibConstants.MODID, "client_connected");

    private final ServerPacketReceiverRegistry registry;
    private final ClientConnectedPacketReceiver clientConnectedPacketReceiver;
    private final LocalizedClients localizedClients;

    @Inject
    public FabricNetworkEvents(
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
