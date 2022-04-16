package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

@Implementation(environment = "CLIENT")
public final class FabricServerboundPacketSender implements ServerboundSender
{
    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {
        if (ClientPlayNetworking.canSend(specification.getPacketID())) {
            ClientPlayNetworking.send(specification.getPacketID(), packetContents);
        } else if (!specification.shouldSilentlyFailOnMissingReceiver()) {
            throw new IllegalStateException(String.format(
                "The server is missing a receiver for packet %s.",
                specification.getPacketID().toString()
            ));
        }
    }
}
