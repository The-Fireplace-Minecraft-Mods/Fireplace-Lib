package dev.the_fireplace.lib.api.network.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public interface ClientPacketReceiver extends ClientPlayNetworking.PlayChannelHandler
{
    Identifier getId();
}
