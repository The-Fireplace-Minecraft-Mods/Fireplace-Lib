package dev.the_fireplace.playtest.network;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.clientbound.PingResponse;
import dev.the_fireplace.playtest.network.clientbound.SecondPingResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.resources.ResourceLocation;

@Singleton
public final class ClientboundPackets
{
    public static final ResourceLocation PING_RESPONSE = new ResourceLocation(PlaytestConstants.MODID, "ping_response");
    public static final ResourceLocation SECOND_PING_RESPONSE = new ResourceLocation(PlaytestConstants.MODID, "second_ping_response");

    private final ClientboundPacketSpecification pingResponseSpec;
    private final ClientboundPacketSpecification secondPingResponseSpec;

    @Inject
    public ClientboundPackets(PingResponse pingResponseSpec, SecondPingResponse secondPingResponseSpec) {
        this.pingResponseSpec = pingResponseSpec;
        this.secondPingResponseSpec = secondPingResponseSpec;
    }

    public ClientboundPacketSpecification getPingResponseSpec() {
        return pingResponseSpec;
    }

    public ClientboundPacketSpecification getSecondPingResponseSpec() {
        return secondPingResponseSpec;
    }
}
