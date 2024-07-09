package dev.the_fireplace.playtest.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.playtest.network.ClientboundPackets;
import dev.the_fireplace.playtest.network.SimplePacketBuilder;
import dev.the_fireplace.playtest.network.clientbound.PingResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

@Singleton
public final class PingCommand implements RegisterableCommand
{
    private final Requirements requirements;
    private final PacketSender packetSender;
    private final ClientboundPackets clientboundPackets;
    private final SimplePacketBuilder simplePacketBuilder;
    private final EmptyUUID emptyUUID;

    @Inject
    public PingCommand(
        Requirements requirements,
        PacketSender packetSender,
        ClientboundPackets clientboundPackets,
        SimplePacketBuilder simplePacketBuilder,
        EmptyUUID emptyUUID
    ) {
        this.requirements = requirements;
        this.packetSender = packetSender;
        this.clientboundPackets = clientboundPackets;
        this.simplePacketBuilder = simplePacketBuilder;
        this.emptyUUID = emptyUUID;
    }


    @Override
    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        return commandDispatcher.register(Commands.literal("ping")
            .requires(requirements::player)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        ServerPlayer serverPlayer = command.getSource().getPlayerOrException();
        serverPlayer.sendMessage(new TextComponent("Ping command received, sending response."), emptyUUID.get());
        packetSender.sendToClient(
            serverPlayer.connection,
            clientboundPackets.getPingResponseSpec(),
            simplePacketBuilder.build(PingResponse.PAYLOAD)
        );

        return Command.SINGLE_SUCCESS;
    }
}
