package dev.the_fireplace.lib.api.network.interfaces;

import net.minecraft.resources.Identifier;

public interface PacketSpecification
{
    Identifier getPacketID();

    default boolean shouldSilentlyFailOnMissingReceiver() {
        return false;
    }
}
