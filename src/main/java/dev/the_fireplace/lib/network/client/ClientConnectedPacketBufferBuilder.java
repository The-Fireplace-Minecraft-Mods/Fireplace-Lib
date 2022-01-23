package dev.the_fireplace.lib.network.client;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.PacketByteBuf;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public final class ClientConnectedPacketBufferBuilder
{
    public PacketByteBuf build(Collection<String> availableTranslators) {
        PacketByteBuf buffer = PacketByteBufs.create();
        for (String translatorModid : availableTranslators) {
            buffer.writeString(translatorModid);
        }

        return buffer;
    }
}
