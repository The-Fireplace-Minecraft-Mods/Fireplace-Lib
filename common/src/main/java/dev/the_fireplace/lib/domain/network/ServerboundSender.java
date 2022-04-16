package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraft.network.FriendlyByteBuf;

public interface ServerboundSender
{
    void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents);
}
