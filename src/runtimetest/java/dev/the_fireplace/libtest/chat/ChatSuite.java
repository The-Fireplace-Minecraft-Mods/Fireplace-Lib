package dev.the_fireplace.libtest.chat;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.libtest.setup.TestSuite;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;

public final class ChatSuite extends TestSuite
{
    private final MessageQueueTest messageQueueTest;
    private final MultilineMessageBufferTest multilineMessageBufferTest;

    @Inject
    public ChatSuite(MessageQueueTest messageQueueTest, MultilineMessageBufferTest multilineMessageBufferTest) {
        this.messageQueueTest = messageQueueTest;
        this.multilineMessageBufferTest = multilineMessageBufferTest;
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> commandContext) {
        CommandOutput commandOutput = getCommandOutput(commandContext);
        messageQueueTest.execute(commandOutput);
        multilineMessageBufferTest.execute(commandOutput);
    }
}
