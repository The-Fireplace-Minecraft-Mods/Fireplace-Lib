package dev.the_fireplace.libtest.chat;

import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;

public final class MessageQueueTest
{
    private final MessageQueue messageQueue;

    @Inject
    public MessageQueueTest(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void execute(CommandOutput commandOutput) {
        test_queueMessages_doesNotThrowException(commandOutput);
    }

    private void test_queueMessages_doesNotThrowException(CommandOutput commandOutput) {
        ArrayList<Text> texts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            texts.add(Text.of("Message Queued Text " + i));
        }
        this.messageQueue.queueMessages(commandOutput, texts.toArray(new Text[]{}));
    }
}
