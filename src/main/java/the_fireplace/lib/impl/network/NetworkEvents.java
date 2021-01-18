package the_fireplace.lib.impl.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import the_fireplace.lib.impl.FireplaceLib;
import the_fireplace.lib.impl.chat.LocalizedClients;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NetworkEvents {
    static final Identifier CLIENT_CONNECTED_CHANNEL_NAME = new Identifier(FireplaceLib.MODID, "client_connected");
    
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_CONNECTED_CHANNEL_NAME, (server, player, networkHandler, packetByteBuf, packetSender) -> {
            Set<String> modids = new HashSet<>();
            while (packetByteBuf.isReadable()) {
                modids.add(packetByteBuf.readString());
            }
            LocalizedClients.addPlayer(player.getUuid(), modids);
        });
    }

    public static void onDisconnected(UUID player) {
        LocalizedClients.removePlayer(player);
    }

    static PacketByteBuf createPacketBuffer(){
        return new PacketByteBuf(Unpooled.buffer());
    }
}
