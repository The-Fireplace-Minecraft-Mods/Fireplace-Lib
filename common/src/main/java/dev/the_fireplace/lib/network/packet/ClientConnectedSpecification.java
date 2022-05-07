package dev.the_fireplace.lib.network.packet;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.lib.network.ServerboundPackets;
import dev.the_fireplace.lib.network.server.ClientConnectedPacketReceiver;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class ClientConnectedSpecification implements ServerboundPacketSpecification
{
    @Override
    public ResourceLocation getPacketID() {
        return ServerboundPackets.CLIENT_CONNECTED_ID;
    }

    @Override
    public boolean shouldSilentlyFailOnMissingReceiver() {
        return true;
    }

    @Override
    public Supplier<ServerboundPacketReceiver> getReceiverFactory() {
        return () -> DIContainer.get().getInstance(ClientConnectedPacketReceiver.class);
    }
}
