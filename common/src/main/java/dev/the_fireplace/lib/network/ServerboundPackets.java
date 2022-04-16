package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.lib.network.packet.ClientConnectedSpecification;
import net.minecraft.resources.ResourceLocation;

import javax.inject.Inject;

public final class ServerboundPackets
{
    public static final ResourceLocation CLIENT_CONNECTED_ID = new ResourceLocation(FireplaceLibConstants.MODID, "client_connected");

    private final ServerboundPacketSpecification clientConnected;

    @Inject
    public ServerboundPackets(ClientConnectedSpecification clientConnected) {
        this.clientConnected = clientConnected;
    }

    public ServerboundPacketSpecification clientConnected() {
        return clientConnected;
    }
}
