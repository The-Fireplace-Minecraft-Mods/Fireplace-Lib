package the_fireplace.lib.api.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import the_fireplace.lib.api.multithreading.ConcurrentExecutionManager;
import the_fireplace.lib.api.util.EmptyUUID;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public final class SynchronizedMessageQueue {
    private static final Map<CommandOutput, SynchronizedMessageQueue> MESSAGE_QUEUES = new ConcurrentHashMap<>();

    private static SynchronizedMessageQueue getOrCreateQueue(CommandOutput messageTarget) {
        return MESSAGE_QUEUES.computeIfAbsent(messageTarget, SynchronizedMessageQueue::new);
    }

    public static void queueMessages(CommandOutput messageTarget, Text... messages) {
        getOrCreateQueue(messageTarget).queueMessages(messages);
    }

    private final Queue<Text> messages = new ArrayDeque<>();
    private final CommandOutput messageTarget;
    private boolean sendingMessages = false;

    private SynchronizedMessageQueue(CommandOutput messageTarget) {
        this.messageTarget = messageTarget;
    }

    private synchronized void queueMessages(Text... messages) {
        this.messages.addAll(Arrays.asList(messages));
        if (!sendingMessages)
            ConcurrentExecutionManager.runKillable(this::sendMessages);
    }

    private synchronized void sendMessages() {
        sendingMessages = true;
        while (!messages.isEmpty()) {
            messageTarget.sendSystemMessage(messages.remove(), EmptyUUID.EMPTY_UUID);
        }
        sendingMessages = false;
    }
}