package dev.the_fireplace.playtest.network.serverbound;

import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.ServerboundPackets;
import dev.the_fireplace.playtest.network.serverbound.reciever.ThirdPingReceiver;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class ThirdPing implements ServerboundPacketSpecification
{
    public static final String PAYLOAD = "Third ping!";

    @Override
    public Supplier<ServerboundPacketReceiver> getReceiverFactory() {
        return () -> PlaytestConstants.getInjector().getInstance(ThirdPingReceiver.class);
    }

    @Override
    public ResourceLocation getPacketID() {
        return ServerboundPackets.THIRD_PING;
    }
}
