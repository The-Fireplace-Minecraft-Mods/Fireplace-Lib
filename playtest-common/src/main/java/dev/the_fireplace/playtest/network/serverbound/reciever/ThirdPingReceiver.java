package dev.the_fireplace.playtest.network.serverbound.reciever;

import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.playtest.network.serverbound.ThirdPing;
import jakarta.inject.Singleton;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Singleton
public final class ThirdPingReceiver implements ServerboundPacketReceiver
{
    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf) {
        if (!buf.readUtf().equals(ThirdPing.PAYLOAD)) {
            throw new Error("Received unexpected payload from third ping!");
        }
        server.execute(() -> {
            player.sendMessage(new TextComponent("Third ping received. Networking is fully functional."), ChatType.SYSTEM);
        });
    }
}
