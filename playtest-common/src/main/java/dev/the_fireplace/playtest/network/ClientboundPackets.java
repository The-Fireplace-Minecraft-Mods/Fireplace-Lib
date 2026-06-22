package dev.the_fireplace.playtest.network;

import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.clientbound.PingResponse;
import dev.the_fireplace.playtest.network.clientbound.SecondPingResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.resources.Identifier;

@Singleton
public final class ClientboundPackets
{
    public static final Identifier PING_RESPONSE = Identifier.fromNamespaceAndPath(PlaytestConstants.MODID, "ping_response");
    public static final Identifier SECOND_PING_RESPONSE = Identifier.fromNamespaceAndPath(PlaytestConstants.MODID, "second_ping_response");

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
