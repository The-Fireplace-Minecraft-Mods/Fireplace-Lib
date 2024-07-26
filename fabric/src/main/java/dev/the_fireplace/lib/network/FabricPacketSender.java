package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.FabricChannelManager;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import javax.inject.Inject;

@Implementation
public final class FabricPacketSender implements PacketSender
{
    private final ServerboundSender serverboundSender;
    private final FabricChannelManager channelManager;

    @Inject
    public FabricPacketSender(ServerboundSender serverboundSender, FabricChannelManager channelManager) {
        this.serverboundSender = serverboundSender;
        this.channelManager = channelManager;
    }

    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {
        serverboundSender.sendToServer(specification, packetContents);
    }

    @Override
    public void sendToClient(ServerGamePacketListenerImpl connection, PacketSpecification specification, FriendlyByteBuf packetContents) {
        if (ServerPlayNetworking.canSend(connection, FireplaceLibConstants.PACKET_CHANNEL_ID)) {
            ServerPlayNetworking.send(connection.player, this.channelManager.wrap(specification, packetContents));
        } else if (!specification.shouldSilentlyFailOnMissingReceiver()) {
            throw new IllegalStateException(String.format(
                "Player %s is missing a receiver for packet %s.",
                connection.player.getGameProfile().getName(),
                specification.getPacketID().toString()
            ));
        }
    }
}
