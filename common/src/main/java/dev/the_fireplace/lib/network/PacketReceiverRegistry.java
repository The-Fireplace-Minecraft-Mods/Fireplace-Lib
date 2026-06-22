package dev.the_fireplace.lib.network;

import dev.the_fireplace.lib.api.network.injectables.PacketSpecificationRegistry;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ClientboundPacketSpecification;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketReceiver;
import dev.the_fireplace.lib.api.network.interfaces.ServerboundPacketSpecification;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class PacketReceiverRegistry implements PacketSpecificationRegistry
{
    private final Map<Identifier, Supplier<ClientboundPacketReceiver>> clientReceivers = new ConcurrentHashMap<>();
    private final Map<Identifier, Supplier<ServerboundPacketReceiver>> serverReceivers = new ConcurrentHashMap<>();

    @Override
    public void register(ServerboundPacketSpecification specification) {
        serverReceivers.put(specification.getPacketID(), specification.getReceiverFactory());
    }

    @Override
    public void register(ClientboundPacketSpecification specification) {
        clientReceivers.put(specification.getPacketID(), specification.getReceiverFactory());
    }

    protected Optional<Supplier<ClientboundPacketReceiver>> getClientReceiver(Identifier packetId) {
        return Optional.ofNullable(clientReceivers.get(packetId));
    }

    protected Optional<Supplier<ServerboundPacketReceiver>> getServerReceiver(Identifier packetId) {
        return Optional.ofNullable(serverReceivers.get(packetId));
    }
}
