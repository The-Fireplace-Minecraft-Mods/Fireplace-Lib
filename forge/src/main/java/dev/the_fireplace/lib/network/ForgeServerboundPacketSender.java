package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.inject.Inject;

@Implementation(environment = "CLIENT")
public final class ForgeServerboundPacketSender implements ServerboundSender
{
    private final SimpleChannelManager simpleChannelManager;

    @Inject
    public ForgeServerboundPacketSender(SimpleChannelManager simpleChannelManager) {
        this.simpleChannelManager = simpleChannelManager;
    }

    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {
        SimpleChannel channel = simpleChannelManager.getChannel();
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null && channel.isRemotePresent(connection.getConnection())) {
            channel.send(PacketDistributor.SERVER.with(() -> null), simpleChannelManager.wrap(specification, packetContents));
        } else if (connection == null) {
            throw new IllegalStateException(String.format(
                "Not connected to a server, cannot send packet %s.",
                specification.getPacketID().toString()
            ));
        } else if (!specification.shouldSilentlyFailOnMissingReceiver()) {
            throw new IllegalStateException(String.format(
                "The server is missing a receiver for packet %s.",
                specification.getPacketID().toString()
            ));
        }
    }
}
