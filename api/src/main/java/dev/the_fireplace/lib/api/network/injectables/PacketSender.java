package dev.the_fireplace.lib.api.network.injectables;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface PacketSender
{
    void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents);

    void sendToClient(ServerGamePacketListenerImpl connection, PacketSpecification specification, FriendlyByteBuf packetContents);
}
