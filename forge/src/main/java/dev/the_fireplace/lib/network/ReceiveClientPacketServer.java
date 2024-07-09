package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.lib.domain.network.ReceiveClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

@Implementation(environment = "SERVER")
public final class ReceiveClientPacketServer implements ReceiveClientPacket
{
    @Override
    public void receiveClientPacket(Supplier<ClientboundPacketReceiver> clientReceiver, NetworkEvent.Context context, FriendlyByteBuf packetContents) {

    }
}
