package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public interface SimpleChannelManager
{
    SimpleChannel getChannel();
    void register();
    Object wrap(PacketSpecification specification, FriendlyByteBuf packetContents);
}
