package dev.the_fireplace.lib.network.server.dummy;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.network.interfaces.PacketSpecification;
import dev.the_fireplace.lib.domain.network.ServerboundSender;
import net.minecraft.network.FriendlyByteBuf;

@Implementation(environment = "SERVER")
public final class ServerServerboundSender implements ServerboundSender
{
    @Override
    public void sendToServer(PacketSpecification specification, FriendlyByteBuf packetContents) {

    }
}
