package dev.the_fireplace.playtest.network.clientbound;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.ClientboundPackets;
import dev.the_fireplace.playtest.network.clientbound.receiver.PingResponseReceiver;
import jakarta.inject.Singleton;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Singleton
public final class PingResponse implements ClientboundPacketSpecification
{
    public static final String PAYLOAD = "Pong!";

    @Override
    public Supplier<ClientboundPacketReceiver> getReceiverFactory() {
        return () -> PlaytestConstants.getInjector().getInstance(PingResponseReceiver.class);
    }

    @Override
    public ResourceLocation getPacketID() {
        return ClientboundPackets.PING_RESPONSE;
    }
}
