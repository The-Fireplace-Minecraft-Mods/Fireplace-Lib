package dev.the_fireplace.playtest.network;

import io.netty.buffer.Unpooled;
import jakarta.inject.Singleton;
import net.minecraft.network.FriendlyByteBuf;

@Singleton
public final class SimplePacketBuilder
{
    public FriendlyByteBuf build(String message) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeUtf(message);

        return buffer;
    }
}
