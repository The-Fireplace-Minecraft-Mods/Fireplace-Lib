package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import javax.inject.Inject;

@Implementation
public final class ForgePacketSender implements PacketSender
{
    private final ServerboundSender serverboundSender;
    private final SimpleChannelManager simpleChannelManager;

    @Inject
    public ForgePacketSender(ServerboundSender serverboundSender, SimpleChannelManager simpleChannelManager) {
        this.serverboundSender = serverboundSender;
        this.simpleChannelManager = simpleChannelManager;
    }

    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {
        serverboundSender.sendToServer(specification, packetContents);
    }

    @Override
    public void sendToClient(ServerGamePacketListenerImpl connection, PacketSpecification specification, FriendlyByteBuf packetContents) {
        SimpleChannel channel = simpleChannelManager.getChannel();
        if (channel.isRemotePresent(connection.getConnection())) {
            channel.send(simpleChannelManager.wrap(specification, packetContents), PacketDistributor.PLAYER.with(connection.player));
        } else if (!specification.shouldSilentlyFailOnMissingReceiver()) {
            throw new IllegalStateException(String.format(
                "Player %s is missing a receiver for packet %s.",
                connection.player.getGameProfile().getName(),
                specification.getPacketID().toString()
            ));
        }
    }
}
