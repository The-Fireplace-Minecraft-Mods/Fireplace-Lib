package dev.the_fireplace.lib.impl.network;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.impl.FireplaceLib;
import dev.the_fireplace.lib.impl.translation.LocalizedClients;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NetworkEvents {
    static final Identifier CLIENT_CONNECTED_CHANNEL_NAME = new Identifier(FireplaceLib.MODID, "client_connected");
    
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(CLIENT_CONNECTED_CHANNEL_NAME, (server, player, networkHandler, packetByteBuf, packetSender) -> {
            Set<String> modids = new HashSet<>();
            while (packetByteBuf.isReadable()) {
                modids.add(packetByteBuf.readString(Short.MAX_VALUE));
            }
            DIContainer.get().getInstance(LocalizedClients.class).addPlayer(player.getUuid(), modids);
        });
    }

    public static void onDisconnected(UUID player) {
        DIContainer.get().getInstance(LocalizedClients.class).removePlayer(player);
    }

    static PacketByteBuf createPacketBuffer(){
        return new PacketByteBuf(Unpooled.buffer());
    }
}
