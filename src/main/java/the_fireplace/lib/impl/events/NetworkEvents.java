package the_fireplace.lib.impl.events;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import the_fireplace.lib.api.chat.TranslationService;
import the_fireplace.lib.impl.FireplaceLib;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NetworkEvents {
    private static final Identifier CLIENT_CONNECTED_CHANNEL_NAME = new Identifier(FireplaceLib.MODID, "client_connected");
    
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_CONNECTED_CHANNEL_NAME, (server, player, networkHandler, packetByteBuf, packetSender) -> {
            Set<String> modids = new HashSet<>();
            while (packetByteBuf.isReadable()) {
                modids.add(packetByteBuf.readString());
            }
            TranslationService.addPlayer(player.getUuid(), modids);
        });
    }

    public static void onDisconnected(UUID player) {
        TranslationService.removePlayer(player);
    }

    public static PacketByteBuf createPacketBuffer(){
        return new PacketByteBuf(Unpooled.buffer());
    }

    @Environment(EnvType.CLIENT)
    public static void onConnectToServer() {
        if (ClientPlayNetworking.canSend(CLIENT_CONNECTED_CHANNEL_NAME)) {
            PacketByteBuf buffer = createPacketBuffer();
            for (String modid : TranslationService.availableTranslationServices()) {
                buffer.writeString(modid);
            }
            ClientPlayNetworking.send(CLIENT_CONNECTED_CHANNEL_NAME, buffer);
        }
    }
}
