package dev.the_fireplace.lib.domain.network;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public interface ReceiveClientPacket
{
    void receiveClientPacket(Supplier<ClientboundPacketReceiver> clientReceiver, NetworkEvent.Context context, FriendlyByteBuf packetContents);
}
