package dev.the_fireplace.playtest.network.clientbound.receiver;

import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.playtest.network.SimplePacketBuilder;
import dev.the_fireplace.playtest.network.ServerboundPackets;
import dev.the_fireplace.playtest.network.clientbound.SecondPingResponse;
import dev.the_fireplace.playtest.network.serverbound.ThirdPing;
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
    private final SimplePacketBuilder simplePacketBuilder;

    @Inject
    public SecondPingResponseReceiver(
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
        if (!buf.readUtf().equals(SecondPingResponse.PAYLOAD)) {
            throw new Error("Received unexpected payload from second ping response!");
        }
        client.submit(() -> {
            assert client.player != null;
            client.player.displayClientMessage(Component.literal("Received second ping response! Network is likely functional, firing one more serverbound packet to verify."), false);
            this.packetSender.sendToServer(this.serverboundPackets.getThirdPingSpec(), simplePacketBuilder.build(ThirdPing.PAYLOAD));
        });
    }
}
