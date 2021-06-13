package dev.the_fireplace.lib.impl.network;

import dev.the_fireplace.lib.api.chat.TranslatorFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.PacketByteBuf;

import javax.inject.Inject;

@Environment(EnvType.CLIENT)
public class ClientNetworkEvents {
    private final TranslatorFactory translatorFactory;

    @Inject
    private ClientNetworkEvents(TranslatorFactory translatorFactory) {
        this.translatorFactory = translatorFactory;
    }

    @Environment(EnvType.CLIENT)
    public void onConnectToServer() {
        if (ClientPlayNetworking.canSend(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME)) {
            PacketByteBuf buffer = NetworkEvents.createPacketBuffer();
            for (String modid : translatorFactory.availableTranslators()) {
                buffer.writeString(modid);
            }
            ClientPlayNetworking.send(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME, buffer);
        }
    }
}
