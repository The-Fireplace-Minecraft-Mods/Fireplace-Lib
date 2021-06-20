package dev.the_fireplace.lib.impl.network;

import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.impl.network.client.ClientConnectedPacketBufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import javax.inject.Inject;

@Environment(EnvType.CLIENT)
public final class ClientNetworkEvents {
    private final TranslatorFactory translatorFactory;
    private final ClientConnectedPacketBufferBuilder bufferBuilder;

    @Inject
    private ClientNetworkEvents(TranslatorFactory translatorFactory, ClientConnectedPacketBufferBuilder bufferBuilder) {
        this.translatorFactory = translatorFactory;
        this.bufferBuilder = bufferBuilder;
    }

    @Environment(EnvType.CLIENT)
    public void onConnectToServer() {
        if (ClientPlayNetworking.canSend(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME)) {
            ClientPlayNetworking.send(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME, bufferBuilder.build(translatorFactory.availableTranslators()));
        }
    }
}
