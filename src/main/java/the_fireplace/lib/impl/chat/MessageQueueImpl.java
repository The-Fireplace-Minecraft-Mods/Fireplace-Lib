package the_fireplace.lib.impl.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import the_fireplace.lib.api.chat.MessageQueue;
import the_fireplace.lib.api.multithreading.ExecutionManager;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public final class MessageQueueImpl implements MessageQueue {
    @Deprecated
    public static final MessageQueueImpl INSTANCE = new MessageQueueImpl();
    private final Map<CommandOutput, TargetMessageQueue> messageQueues = new ConcurrentHashMap<>();

    private MessageQueueImpl(){}

    private TargetMessageQueue getOrCreateQueue(CommandOutput messageTarget) {
        return messageQueues.computeIfAbsent(messageTarget, TargetMessageQueue::new);
    }

    @Override
    public void queueMessages(CommandOutput messageTarget, Text... messages) {
        getOrCreateQueue(messageTarget).queueMessages(messages);
    }

    private static class TargetMessageQueue {
        private final Queue<Text> messages = new ArrayDeque<>();
        private final CommandOutput messageTarget;
        private boolean sendingMessages = false;

        private TargetMessageQueue(CommandOutput messageTarget) {
            this.messageTarget = messageTarget;
        }

        private synchronized void queueMessages(Text... messages) {
            this.messages.addAll(Arrays.asList(messages));
            if (!sendingMessages)
                ExecutionManager.getInstance().runKillable(this::sendMessages);
        }

        private synchronized void sendMessages() {
            sendingMessages = true;
            while (!messages.isEmpty()) {
                messageTarget.sendSystemMessage(messages.remove(), null);
            }
            sendingMessages = false;
        }
    }
}