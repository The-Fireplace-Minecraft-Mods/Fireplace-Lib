package dev.the_fireplace.playtest.network;

import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.serverbound.SecondPing;
import dev.the_fireplace.playtest.network.serverbound.ThirdPing;
import jakarta.inject.Inject;
import net.minecraft.resources.Identifier;

public final class ServerboundPackets
{
    public static final Identifier SECOND_PING = Identifier.fromNamespaceAndPath(PlaytestConstants.MODID, "second_ping");
    public static final Identifier THIRD_PING = Identifier.fromNamespaceAndPath(PlaytestConstants.MODID, "third_ping");

    private final ServerboundPacketSpecification secondPingSpec;
    private final ServerboundPacketSpecification thirdPingSpec;

    @Inject
    public ServerboundPackets(SecondPing secondPingSpec, ThirdPing thirdPingSpec) {
        this.secondPingSpec = secondPingSpec;
        this.thirdPingSpec = thirdPingSpec;
    }

    public ServerboundPacketSpecification getSecondPingSpec() {
        return secondPingSpec;
    }

    public ServerboundPacketSpecification getThirdPingSpec() {
        return thirdPingSpec;
    }
}
