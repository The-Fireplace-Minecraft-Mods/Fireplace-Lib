package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.*;
import dev.the_fireplace.lib.domain.network.ReceiveClientPacket;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import io.netty.buffer.Unpooled;
import jakarta.inject.Inject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Singleton
@Implementation(allInterfaces = true)
public final class ForgePacketChannel implements SimpleChannelManager, PacketSpecificationRegistry
{
    private final SimpleChannel CHANNEL = ChannelBuilder
        .named(new ResourceLocation(FireplaceLibConstants.MODID, "packets"))
        .clientAcceptedVersions((a, b) -> true)
        .serverAcceptedVersions((a, b) -> true)
        .networkProtocolVersion(1)
        .simpleChannel();
    private final Map<ResourceLocation, Supplier<ClientboundPacketReceiver>> clientReceivers = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, Supplier<ServerboundPacketReceiver>> serverReceivers = new ConcurrentHashMap<>();
    private final ReceiveClientPacket receiveClientPacket;

    @Inject
    public ForgePacketChannel(ReceiveClientPacket receiveClientPacket) {
        this.receiveClientPacket = receiveClientPacket;
    }

    @Override
    public SimpleChannel getChannel() {
        return CHANNEL;
    }
    
    @Override
    public void register() {
        CHANNEL.messageBuilder(ReceiverWrapper.class)
            .decoder(ReceiverWrapper::new)
            .encoder((receiverWrapper, outputByteBuf) -> outputByteBuf.writeBytes(receiverWrapper.buffer))
            .consumerMainThread(ReceiverWrapper::read)
            .add();
    }

    @Override
    public void register(ServerboundPacketSpecification specification) {
        serverReceivers.put(specification.getPacketID(), specification.getReceiverFactory());
    }

    @Override
    public void register(ClientboundPacketSpecification specification) {
        clientReceivers.put(specification.getPacketID(), specification.getReceiverFactory());
    }

    public Object wrap(PacketSpecification specification, FriendlyByteBuf packetContents) {
        FriendlyByteBuf wrappedPacketContents = new FriendlyByteBuf(Unpooled.buffer());
        wrappedPacketContents.writeResourceLocation(specification.getPacketID());
        wrappedPacketContents.writeBytes(packetContents);

        return new ReceiverWrapper(wrappedPacketContents);
    }

    private class ReceiverWrapper
    {
        private final FriendlyByteBuf buffer;
        private ReceiverWrapper(FriendlyByteBuf buffer) {
            this.buffer = buffer;
        }

        public void read(CustomPayloadEvent.Context context) {
            ResourceLocation packetId = this.buffer.readResourceLocation();
            FriendlyByteBuf packetContents = new FriendlyByteBuf(this.buffer.copy());
            Supplier<ClientboundPacketReceiver> clientReceiver = clientReceivers.get(packetId);
            Supplier<ServerboundPacketReceiver> serverReceiver = serverReceivers.get(packetId);
            receiveClientPacket.receiveClientPacket(clientReceiver, context, packetContents);
            if (serverReceiver != null) {
                ServerPlayer sender = context.getSender();
                if (sender != null) {
                    context.enqueueWork(() ->
                        serverReceiver.get().receive(sender.server, sender, sender.connection, packetContents)
                    );
                }
            }

            context.setPacketHandled(true);
        }
    }
}
