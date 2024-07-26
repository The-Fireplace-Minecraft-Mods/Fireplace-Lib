package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.SimpleChannel;

public interface SimpleChannelManager
{
    SimpleChannel getChannel();
    Object wrap(PacketSpecification specification, FriendlyByteBuf packetContents);
}
