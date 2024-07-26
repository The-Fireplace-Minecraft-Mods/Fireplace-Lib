package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.*;
import dev.the_fireplace.lib.domain.network.NetworkRegister;
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

@Singleton
@Implementation(allInterfaces = true)
public final class ForgePacketChannel extends PacketReceiverRegistry implements SimpleChannelManager, PacketSpecificationRegistry, NetworkRegister
{
    private final SimpleChannel CHANNEL = ChannelBuilder
        .named(FireplaceLibConstants.PACKET_CHANNEL_ID)
        .clientAcceptedVersions((a, b) -> true)
        .serverAcceptedVersions((a, b) -> true)
        .networkProtocolVersion(1)
        .simpleChannel();
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
            getClientReceiver(packetId).ifPresent(
                clientReceiver -> receiveClientPacket.receiveClientPacket(clientReceiver, context, packetContents)
            );
            getServerReceiver(packetId).ifPresent(serverReceiver -> {
                ServerPlayer sender = context.getSender();
                if (sender != null) {
                    context.enqueueWork(() ->
                        serverReceiver.get().receive(sender.server, sender, sender.connection, packetContents)
                    );
                }
            });

            context.setPacketHandled(true);
        }
    }
}
