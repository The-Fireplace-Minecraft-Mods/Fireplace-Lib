package dev.the_fireplace.lib.api.network.server;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public interface ServerPacketReceiver extends ServerPlayNetworking.PlayChannelHandler {
    Identifier getId();
}
