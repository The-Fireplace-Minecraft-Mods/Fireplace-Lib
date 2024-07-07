package dev.the_fireplace.playtest.network.serverbound.reciever;

import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import jakarta.inject.Singleton;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Singleton
public final class ThirdPingReceiver implements ServerboundPacketReceiver
{
    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf) {
        server.execute(() -> {
            player.sendSystemMessage(Component.literal("Third ping received. Networking is fully functional."));
        });
    }
}
