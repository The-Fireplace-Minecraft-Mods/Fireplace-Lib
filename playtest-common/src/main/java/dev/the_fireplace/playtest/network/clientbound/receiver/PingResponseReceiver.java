package dev.the_fireplace.playtest.network.clientbound.receiver;

import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.playtest.network.SimplePacketBuilder;
import dev.the_fireplace.playtest.network.ServerboundPackets;
import dev.the_fireplace.playtest.network.clientbound.PingResponse;
import dev.the_fireplace.playtest.network.serverbound.SecondPing;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

@Singleton
public final class PingResponseReceiver implements ClientboundPacketReceiver
{
    private final PacketSender packetSender;
    private final ServerboundPackets serverboundPackets;
    private final SimplePacketBuilder simplePacketBuilder;

    @Inject
    public PingResponseReceiver(
        PacketSender packetSender,
        ServerboundPackets serverboundPackets,
        SimplePacketBuilder simplePacketBuilder
    ) {
        this.packetSender = packetSender;
        this.serverboundPackets = serverboundPackets;
        this.simplePacketBuilder = simplePacketBuilder;
    }

    @Override
    public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf) {
        if (!buf.readUtf().equals(PingResponse.PAYLOAD)) {
            throw new Error("Received unexpected payload from ping response!");
        }
        client.submit(() -> {
            assert client.player != null;
            client.player.displayClientMessage(new TextComponent("Received first ping response! Pinging again via FL packet."), false);
            this.packetSender.sendToServer(this.serverboundPackets.getSecondPingSpec(), simplePacketBuilder.build(SecondPing.PAYLOAD));
        });
    }
}
