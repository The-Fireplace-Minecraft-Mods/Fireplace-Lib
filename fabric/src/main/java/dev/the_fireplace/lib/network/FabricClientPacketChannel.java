package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.domain.network.FabricChannelManager;
import dev.the_fireplace.lib.domain.network.NetworkRegister;
import jakarta.inject.Singleton;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Implementation(allInterfaces = true, environment = "CLIENT")
@Singleton
public final class FabricClientPacketChannel extends FabricPacketChannel implements FabricChannelManager, PacketSpecificationRegistry, NetworkRegister
{
    protected void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(
            ReceiverWrapper.TYPE,
            (payload, context) -> {
                ReceiverWrapper.Data packetData = payload.unwrap();
                ResourceLocation packetId = packetData.packetId();
                getClientReceiver(packetId).ifPresent(clientReceiver -> {
                    Minecraft client = Minecraft.getInstance();
                    FriendlyByteBuf packetContents = packetData.packetContents();
                    clientReceiver.get().receive(client, client.getConnection(), packetContents);
                    int unreadBytes = packetContents.readableBytes();
                    if (unreadBytes > 0) {
                        FireplaceLibConstants.getLogger().error("Packet {} was larger than expected, found {} bytes extra", packetId.toString(), unreadBytes);
                    }
                });
            }
        );
    }
}
