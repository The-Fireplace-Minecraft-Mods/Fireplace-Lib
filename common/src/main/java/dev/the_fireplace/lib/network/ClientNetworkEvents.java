package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.network.client.ClientConnectedPacketBufferBuilder;

import javax.inject.Inject;

public final class ClientNetworkEvents
{
    private final TranslatorFactory translatorFactory;
    private final ClientConnectedPacketBufferBuilder bufferBuilder;
    private final ServerboundPackets serverboundPackets;
    private final PacketSender packetSender;

    @Inject
    public ClientNetworkEvents(
        TranslatorFactory translatorFactory,
        ClientConnectedPacketBufferBuilder bufferBuilder,
        ServerboundPackets serverboundPackets,
        PacketSender packetSender
    ) {
        this.translatorFactory = translatorFactory;
        this.bufferBuilder = bufferBuilder;
        this.serverboundPackets = serverboundPackets;
        this.packetSender = packetSender;
    }

    public void onConnectToServer() {
        this.packetSender.sendToServer(
            this.serverboundPackets.clientConnected(),
            this.bufferBuilder.build(translatorFactory.availableTranslators())
        );
    }
}
