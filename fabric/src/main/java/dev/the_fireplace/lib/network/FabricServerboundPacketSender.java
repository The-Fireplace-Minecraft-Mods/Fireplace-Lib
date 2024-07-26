package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.FabricChannelManager;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import jakarta.inject.Inject;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

@Implementation(environment = "CLIENT")
public final class FabricServerboundPacketSender implements ServerboundSender
{
    private final FabricChannelManager channelManager;

    @Inject
    public FabricServerboundPacketSender(FabricChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {
        if (ClientPlayNetworking.canSend(FireplaceLibConstants.PACKET_CHANNEL_ID)) {
            ClientPlayNetworking.send(this.channelManager.wrap(specification, packetContents));
        } else if (!specification.shouldSilentlyFailOnMissingReceiver()) {
            throw new IllegalStateException(String.format(
                "The server is missing a receiver for packet %s.",
                specification.getPacketID().toString()
            ));
        }
    }
}
