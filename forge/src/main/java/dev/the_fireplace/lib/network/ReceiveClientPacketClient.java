package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.lib.domain.network.ReceiveClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

@Implementation(environment = "CLIENT")
public final class ReceiveClientPacketClient implements ReceiveClientPacket
{
    @Override
    public void receiveClientPacket(Supplier<ClientboundPacketReceiver> clientReceiver, CustomPayloadEvent.Context context, FriendlyByteBuf packetContents) {
        context.enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                clientReceiver.get().receive(Minecraft.getInstance(), Minecraft.getInstance().getConnection(), packetContents)
            )
        );
    }
}
