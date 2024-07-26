package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.*;
import dev.the_fireplace.lib.domain.network.ClientboundReceiverRegistry;
import dev.the_fireplace.lib.domain.network.FabricChannelManager;
import dev.the_fireplace.lib.domain.network.NetworkRegister;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.CommonVersionPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.function.Supplier;

@Implementation(allInterfaces = true, environment = "SERVER")
@Singleton
public class FabricPacketChannel extends PacketReceiverRegistry implements FabricChannelManager, PacketSpecificationRegistry, NetworkRegister
{

    @Override
    public final void register() {
        PayloadTypeRegistry.playC2S().register(
            ReceiverWrapper.TYPE,
            ReceiverWrapper.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
            ReceiverWrapper.TYPE,
            ReceiverWrapper.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
            ReceiverWrapper.TYPE,
            (payload, context) -> {
                ReceiverWrapper.Data packetData = payload.unwrap();
                ResourceLocation packetId = packetData.packetId();
                getServerReceiver(packetId).ifPresent(serverReceiver -> {
                    ServerPlayer sender = context.player();
                    if (sender != null) {
                        FriendlyByteBuf packetContents = packetData.packetContents();
                        serverReceiver.get().receive(sender.server, sender, sender.connection, packetContents);
                        int unreadBytes = packetContents.readableBytes();
                        if (unreadBytes > 0) {
                            FireplaceLibConstants.getLogger().error("Packet {} was larger than expected, found {} bytes extra", packetId.toString(), unreadBytes);
                        }
                    }
                });
            }
        );
        registerClient();
    }

    protected void registerClient() {}

    @Override
    public CustomPacketPayload wrap(PacketSpecification specification, FriendlyByteBuf packetContents) {
        FriendlyByteBuf wrappedPacketContents = new FriendlyByteBuf(Unpooled.buffer());
        wrappedPacketContents.writeResourceLocation(specification.getPacketID());
        wrappedPacketContents.writeBytes(packetContents);

        return new ReceiverWrapper(wrappedPacketContents);
    }

    protected static class ReceiverWrapper implements CustomPacketPayload
    {
        private static final StreamCodec<FriendlyByteBuf, ReceiverWrapper> CODEC = CustomPacketPayload.codec(
            (receiverWrapper, outputByteBuf) -> outputByteBuf.writeBytes(receiverWrapper.buffer),
            ReceiverWrapper::new
        );
        protected static final CustomPacketPayload.Type<ReceiverWrapper> TYPE = CustomPacketPayload.createType(
            FireplaceLibConstants.PACKET_CHANNEL_ID.toString()
        );

        private final FriendlyByteBuf buffer;
        private ReceiverWrapper(FriendlyByteBuf buffer) {
            this.buffer = new FriendlyByteBuf(buffer.copy());
            buffer.clear();
        }

        protected record Data(ResourceLocation packetId, FriendlyByteBuf packetContents) {}

        protected Data unwrap() {
            ResourceLocation packetId = this.buffer.readResourceLocation();
            FriendlyByteBuf packetContents = new FriendlyByteBuf(this.buffer.copy());
            return new Data(packetId, packetContents);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
