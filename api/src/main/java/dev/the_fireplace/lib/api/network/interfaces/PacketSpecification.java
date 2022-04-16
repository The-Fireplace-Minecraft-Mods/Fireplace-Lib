package dev.the_fireplace.lib.api.network.interfaces;

import net.minecraft.resources.ResourceLocation;

public interface PacketSpecification
{
    ResourceLocation getPacketID();

    default boolean shouldSilentlyFailOnMissingReceiver() {
        return false;
    }
}
