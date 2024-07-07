package dev.the_fireplace.playtest.network.serverbound.reciever;

import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.playtest.network.ClientboundPackets;
import dev.the_fireplace.playtest.network.EmptyPacketBuilder;
import dev.the_fireplace.playtest.network.ServerboundPackets;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Singleton
public final class SecondPingReceiver implements ServerboundPacketReceiver
{
    private final PacketSender packetSender;
    private final ClientboundPackets clientboundPackets;
    private final EmptyPacketBuilder emptyPacketBuilder;

    @Inject
    public SecondPingReceiver(
        PacketSender packetSender,
        ClientboundPackets clientboundPackets,
        EmptyPacketBuilder emptyPacketBuilder
    ) {
        this.packetSender = packetSender;
        this.clientboundPackets = clientboundPackets;
        this.emptyPacketBuilder = emptyPacketBuilder;
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf) {
        server.execute(() -> {
            player.sendSystemMessage(Component.literal("Second ping received. Issuing clientbound response now."));
            this.packetSender.sendToClient(player.connection, this.clientboundPackets.getSecondPingResponseSpec(), this.emptyPacketBuilder.build());
        });
    }
}
