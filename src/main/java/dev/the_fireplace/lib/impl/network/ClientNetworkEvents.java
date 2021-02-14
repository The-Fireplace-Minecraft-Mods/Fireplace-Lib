package dev.the_fireplace.lib.impl.network;

import dev.the_fireplace.lib.api.chat.TranslatorManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class ClientNetworkEvents {
    @Environment(EnvType.CLIENT)
    public static void onConnectToServer() {
        if (ClientPlayNetworking.canSend(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME)) {
            PacketByteBuf buffer = NetworkEvents.createPacketBuffer();
            for (String modid : TranslatorManager.getInstance().availableTranslators()) {
                buffer.writeString(modid);
            }
            ClientPlayNetworking.send(NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME, buffer);
        }
    }
}
