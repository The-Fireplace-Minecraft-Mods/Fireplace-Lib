package dev.the_fireplace.playtest.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;
import dev.the_fireplace.lib.api.network.injectables.PacketSender;
import dev.the_fireplace.playtest.PlaytestConstants;
import dev.the_fireplace.playtest.network.ClientboundPackets;
import dev.the_fireplace.playtest.network.EmptyPacketBuilder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Singleton
public final class PingCommand implements RegisterableCommand
{
    private final Requirements requirements;
    private final PacketSender packetSender;
    private final ClientboundPackets clientboundPackets;
    private final EmptyPacketBuilder emptyPacketBuilder;

    @Inject
    public PingCommand(
        Requirements requirements,
        PacketSender packetSender,
        ClientboundPackets clientboundPackets,
        EmptyPacketBuilder emptyPacketBuilder
    ) {
        this.requirements = requirements;
        this.packetSender = packetSender;
        this.clientboundPackets = clientboundPackets;
        this.emptyPacketBuilder = emptyPacketBuilder;
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
        serverPlayer.sendSystemMessage(Component.literal("Ping command received, sending response."));
        packetSender.sendToClient(serverPlayer.connection, clientboundPackets.getPingResponseSpec(), emptyPacketBuilder.build());

        return Command.SINGLE_SUCCESS;
    }
}
