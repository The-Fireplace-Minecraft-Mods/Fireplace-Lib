package dev.the_fireplace.lib.chat;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

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
public final class MessageQueueImpl implements MessageQueue
{
    private final Map<CommandSource, TargetMessageQueue> messageQueues = new ConcurrentHashMap<>();
    private final ExecutionManager executionManager;
    private final EmptyUUID emptyUUID;

    @Inject
    public MessageQueueImpl(ExecutionManager executionManager, EmptyUUID emptyUUID) {
        this.executionManager = executionManager;
        this.emptyUUID = emptyUUID;
    }

    private TargetMessageQueue getOrCreateQueue(CommandSource messageTarget) {
        return messageQueues.computeIfAbsent(messageTarget, TargetMessageQueue::new);
    }

    @Override
    public void queueMessages(CommandSource messageTarget, Component... messages) {
        getOrCreateQueue(messageTarget).queueMessages(messages);
    }

    @ThreadSafe
    private class TargetMessageQueue
    {
        private final Queue<Component> messages = new ArrayDeque<>();
        private final CommandSource messageTarget;
        private final AtomicBoolean sendingMessages = new AtomicBoolean(false);

        private TargetMessageQueue(CommandSource messageTarget) {
            this.messageTarget = messageTarget;
        }

        private synchronized void queueMessages(Component... messages) {
            this.messages.addAll(Arrays.asList(messages));
            if (!sendingMessages.get()) {
                MessageQueueImpl.this.executionManager.runKillable(this::sendMessages);
            }
        }

        private synchronized void sendMessages() {
            sendingMessages.set(true);
            while (!messages.isEmpty()) {
                messageTarget.sendMessage(messages.remove(), emptyUUID.get());
            }
            sendingMessages.set(false);
        }
    }
}