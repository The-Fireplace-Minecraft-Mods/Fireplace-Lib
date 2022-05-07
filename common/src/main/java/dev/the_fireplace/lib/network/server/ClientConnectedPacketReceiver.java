package dev.the_fireplace.lib.network.server;

import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.lib.domain.translation.LocalizedClients;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public final class ClientConnectedPacketReceiver implements ServerboundPacketReceiver
{
    private final LocalizedClients localizedClients;

    @Inject
    public ClientConnectedPacketReceiver(LocalizedClients localizedClients) {
        this.localizedClients = localizedClients;
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf) {
        Set<String> modids = new HashSet<>();
        while (buf.isReadable()) {
            modids.add(buf.readUtf(Short.MAX_VALUE));
        }
        localizedClients.addPlayer(player.getUUID(), modids);
    }
}
