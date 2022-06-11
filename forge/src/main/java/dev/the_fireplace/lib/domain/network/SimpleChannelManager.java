package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public interface SimpleChannelManager
{
    SimpleChannel getChannel(PacketSpecification specification);
}
