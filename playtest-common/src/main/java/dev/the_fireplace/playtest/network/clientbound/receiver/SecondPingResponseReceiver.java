package dev.the_fireplace.playtest.network.clientbound.receiver;

import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.playtest.network.EmptyPacketBuilder;
import dev.the_fireplace.playtest.network.ServerboundPackets;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

@Singleton
public final class SecondPingResponseReceiver implements ClientboundPacketReceiver
{
    private final PacketSender packetSender;
    private final ServerboundPackets serverboundPackets;
    private final EmptyPacketBuilder emptyPacketBuilder;

    @Inject
    public SecondPingResponseReceiver(
        PacketSender packetSender,
        ServerboundPackets serverboundPackets,
        EmptyPacketBuilder emptyPacketBuilder
    ) {
        this.packetSender = packetSender;
        this.serverboundPackets = serverboundPackets;
        this.emptyPacketBuilder = emptyPacketBuilder;
    }

    @Override
    public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf) {
        client.submit(() -> {
            assert client.player != null;
            client.player.displayClientMessage(Component.literal("Received second ping response! Network is likely functional, firing one more serverbound packet to verify."), false);
            this.packetSender.sendToServer(this.serverboundPackets.getThirdPingSpec(), emptyPacketBuilder.build());
        });
    }
}
