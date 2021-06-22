package dev.the_fireplace.lib.chat;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@ThreadSafe
@Implementation
@Singleton
public final class MessageQueueImpl implements MessageQueue {
    private final Map<CommandOutput, TargetMessageQueue> messageQueues = new ConcurrentHashMap<>();
    private final ExecutionManager executionManager;
    private final EmptyUUID emptyUUID;

    @Inject
    private MessageQueueImpl(ExecutionManager executionManager, EmptyUUID emptyUUID) {
        this.executionManager = executionManager;
        this.emptyUUID = emptyUUID;
    }

    private TargetMessageQueue getOrCreateQueue(CommandOutput messageTarget) {
        return messageQueues.computeIfAbsent(messageTarget, TargetMessageQueue::new);
    }

    @Override
    public void queueMessages(CommandOutput messageTarget, Text... messages) {
        getOrCreateQueue(messageTarget).queueMessages(messages);
    }

    @ThreadSafe
    private class TargetMessageQueue {
        private final Queue<Text> messages = new ArrayDeque<>();
        private final CommandOutput messageTarget;
        private final AtomicBoolean sendingMessages = new AtomicBoolean(false);

        private TargetMessageQueue(CommandOutput messageTarget) {
            this.messageTarget = messageTarget;
        }

        private synchronized void queueMessages(Text... messages) {
            this.messages.addAll(Arrays.asList(messages));
            if (!sendingMessages.get()) {
                MessageQueueImpl.this.executionManager.runKillable(this::sendMessages);
            }
        }

        private synchronized void sendMessages() {
            sendingMessages.set(true);
            while (!messages.isEmpty()) {
                messageTarget.sendMessage(messages.remove());
            }
            sendingMessages.set(false);
        }
    }
}