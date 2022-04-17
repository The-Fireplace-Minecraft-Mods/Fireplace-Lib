package dev.the_fireplace.lib.api.network.interfaces;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public interface ServerPacketReceiver extends ServerPlayNetworking.PlayChannelHandler
{
    ResourceLocation getId();
}
