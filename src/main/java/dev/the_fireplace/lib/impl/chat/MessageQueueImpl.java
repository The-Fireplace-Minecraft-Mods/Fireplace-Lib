package dev.the_fireplace.lib.impl.chat;

import dev.the_fireplace.lib.api.chat.MessageQueue;
import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.util.EmptyUUID;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@ThreadSafe
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

    @ThreadSafe
    private static class TargetMessageQueue {
        private final Queue<Text> messages = new ArrayDeque<>();
        private final CommandOutput messageTarget;
        private final AtomicBoolean sendingMessages = new AtomicBoolean(false);

        private TargetMessageQueue(CommandOutput messageTarget) {
            this.messageTarget = messageTarget;
        }

        private synchronized void queueMessages(Text... messages) {
            this.messages.addAll(Arrays.asList(messages));
            if (!sendingMessages.get()) {
                ExecutionManager.getInstance().runKillable(this::sendMessages);
            }
        }

        private synchronized void sendMessages() {
            sendingMessages.set(true);
            while (!messages.isEmpty()) {
                messageTarget.sendSystemMessage(messages.remove(), EmptyUUID.EMPTY_UUID);
            }
            sendingMessages.set(false);
        }
    }
}