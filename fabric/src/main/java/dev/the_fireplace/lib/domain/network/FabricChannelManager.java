package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface FabricChannelManager
{
    CustomPacketPayload wrap(PacketSpecification specification, FriendlyByteBuf packetContents);
}
