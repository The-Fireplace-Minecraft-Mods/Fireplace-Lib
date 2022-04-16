package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Supplier;

@Implementation
@Singleton
public final class ForgePacketSpecificationRegistry implements PacketSpecificationRegistry
{
    private final ClientboundReceiverRegistry clientboundReceiverRegistry;
    private final SimpleChannelManager simpleChannelManager;

    @Inject
    public ForgePacketSpecificationRegistry(ClientboundReceiverRegistry clientboundReceiverRegistry, SimpleChannelManager simpleChannelManager) {
        this.clientboundReceiverRegistry = clientboundReceiverRegistry;
        this.simpleChannelManager = simpleChannelManager;
    }

    @Override
    public void register(ServerboundPacketSpecification specification) {
        this.simpleChannelManager.getChannel(specification).registerMessage(
            1,
            ReceiverWrapper.class,
            (unused, unused2) -> new FriendlyByteBuf(Unpooled.buffer()),
            buffer -> new ReceiverWrapper(specification.getReceiverFactory().get(), buffer),
            ReceiverWrapper::read
        );
    }

    @Override
    public void register(ClientboundPacketSpecification specification) {
        this.clientboundReceiverRegistry.registerReceiver(specification);
    }

    private static class ReceiverWrapper
    {
        private final ServerboundPacketReceiver receiver;
        private final FriendlyByteBuf buffer;

        private ReceiverWrapper(ServerboundPacketReceiver receiver, FriendlyByteBuf buffer) {
            this.receiver = receiver;
            this.buffer = buffer;
        }

        public void read(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                context.enqueueWork(() ->
                    this.receiver.receive(sender.server, sender, sender.connection, this.buffer)
                );
            }
            context.setPacketHandled(true);
        }
    }
}
