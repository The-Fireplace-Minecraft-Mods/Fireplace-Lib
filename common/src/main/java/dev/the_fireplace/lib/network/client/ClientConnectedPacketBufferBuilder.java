package dev.the_fireplace.lib.network.client;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public final class ClientConnectedPacketBufferBuilder
{
    public FriendlyByteBuf build(Collection<String> availableTranslators) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        for (String translatorModid : availableTranslators) {
            buffer.writeUtf(translatorModid);
        }

        return buffer;
    }
}
