package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.domain.network.ClientNetworkEvents;
import dev.the_fireplace.lib.network.client.ClientConnectedPacketBufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import javax.inject.Inject;

@Environment(EnvType.CLIENT)
@Implementation(environment = "CLIENT")
public final class FabricClientNetworkEvents implements ClientNetworkEvents
{
    private final TranslatorFactory translatorFactory;
    private final ClientConnectedPacketBufferBuilder bufferBuilder;

    @Inject
    private FabricClientNetworkEvents(TranslatorFactory translatorFactory, ClientConnectedPacketBufferBuilder bufferBuilder) {
        this.translatorFactory = translatorFactory;
        this.bufferBuilder = bufferBuilder;
    }

    @Override
    public void onConnectToServer() {
        if (ClientPlayNetworking.canSend(FabricNetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME)) {
            ClientPlayNetworking.send(FabricNetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME, bufferBuilder.build(translatorFactory.availableTranslators()));
        }
    }
}
