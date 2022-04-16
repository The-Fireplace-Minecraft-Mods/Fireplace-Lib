package dev.the_fireplace.lib.api.network.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Client side only.
 */
public interface ClientboundPacketReceiver
{
    void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf);
}
