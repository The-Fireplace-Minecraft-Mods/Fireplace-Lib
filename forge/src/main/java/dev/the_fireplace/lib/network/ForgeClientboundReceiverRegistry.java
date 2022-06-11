package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Supplier;

@Implementation(environment = "CLIENT")
@Singleton
public final class ForgeClientboundReceiverRegistry implements ClientboundReceiverRegistry
{
    private final SimpleChannelManager simpleChannelManager;

    @Inject
    public ForgeClientboundReceiverRegistry(SimpleChannelManager simpleChannelManager) {
        this.simpleChannelManager = simpleChannelManager;
    }

    @Override
    public void registerReceiver(ClientboundPacketSpecification specification) {
        this.simpleChannelManager.getChannel(specification).registerMessage(
            1,
            ReceiverWrapper.class,
            (unused, unused2) -> new FriendlyByteBuf(Unpooled.buffer()),
            buffer -> new ReceiverWrapper(specification.getReceiverFactory().get(), buffer),
            ReceiverWrapper::read
        );
    }

    private static class ReceiverWrapper
    {
        private final ClientboundPacketReceiver receiver;
        private final FriendlyByteBuf buffer;
        private final Minecraft client;

        private ReceiverWrapper(ClientboundPacketReceiver receiver, FriendlyByteBuf buffer) {
            this.receiver = receiver;
            this.buffer = buffer;
            this.client = Minecraft.getInstance();
        }

        public void read(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    this.receiver.receive(client, client.getConnection(), this.buffer)
                )
            );
            context.setPacketHandled(true);
        }
    }
}
