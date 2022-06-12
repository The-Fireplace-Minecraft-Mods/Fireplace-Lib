package dev.the_fireplace.lib.network;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.SimpleChannelManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Implementation
public final class SimpleChannelManagerImpl implements SimpleChannelManager
{
    private final Map<ResourceLocation, SimpleChannel> registeredChannels = new ConcurrentHashMap<>();

    @Override
    public SimpleChannel getChannel(PacketSpecification specification) {
        return registeredChannels.computeIfAbsent(specification.getPacketID(), channelName -> {
            SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                channelName,
                () -> "",
                version -> true,
                version -> true
            );
            registerByteBufMessageEncoder(channel);

            return channel;
        });
    }

    private void registerByteBufMessageEncoder(SimpleChannel channel) {
        channel.registerMessage(
            1,
            FriendlyByteBuf.class,
            (inputByteBuf, outputByteBuf) -> outputByteBuf.writeBytes(inputByteBuf),
            buffer -> buffer,
            (friendlyByteBuf, contextSupplier) -> {
            }
        );
    }
}
