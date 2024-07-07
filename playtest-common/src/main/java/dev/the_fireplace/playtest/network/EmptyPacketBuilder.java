package dev.the_fireplace.playtest.network;

import io.netty.buffer.Unpooled;
import jakarta.inject.Singleton;
import net.minecraft.network.FriendlyByteBuf;

@Singleton
public final class EmptyPacketBuilder
{
    public FriendlyByteBuf build() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }
}
