package dev.the_fireplace.lib.network.server;

import dev.the_fireplace.lib.api.network.interfaces.ServerPacketReceiver;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import dev.the_fireplace.lib.network.NetworkEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public final class ClientConnectedPacketReceiver implements ServerPacketReceiver {

    private final LocalizedClients localizedClients;

    @Inject
    public ClientConnectedPacketReceiver(LocalizedClients localizedClients) {
        this.localizedClients = localizedClients;
    }

    @Override
    public Identifier getId() {
        return NetworkEvents.CLIENT_CONNECTED_CHANNEL_NAME;
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Set<String> modids = new HashSet<>();
        while (buf.isReadable()) {
            modids.add(buf.readString(Short.MAX_VALUE));
        }
        localizedClients.addPlayer(player.getUuid(), modids);
    }
}
