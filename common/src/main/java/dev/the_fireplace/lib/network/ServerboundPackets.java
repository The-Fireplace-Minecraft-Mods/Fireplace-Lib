package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.lib.network.packet.ClientConnectedSpecification;
import net.minecraft.resources.Identifier;

import javax.inject.Inject;

public final class ServerboundPackets
{
    public static final Identifier CLIENT_CONNECTED_ID = Identifier.fromNamespaceAndPath(FireplaceLibConstants.MODID, "client_connected");

    private final ServerboundPacketSpecification clientConnected;

    @Inject
    public ServerboundPackets(ClientConnectedSpecification clientConnected) {
        this.clientConnected = clientConnected;
    }

    public ServerboundPacketSpecification clientConnected() {
        return clientConnected;
    }
}
