package the_fireplace.lib.api.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MultilineMessageBuffer {
    private static final Map<Integer, MultilineMessageBuffer> MESSAGE_BUFFERS = new ConcurrentHashMap<>();
    private static int currentBufferId = Integer.MIN_VALUE;

    public static synchronized int create(byte expectedMessageCount, CommandOutput target) {
        MESSAGE_BUFFERS.put(currentBufferId, new MultilineMessageBuffer(currentBufferId, expectedMessageCount, target));
        return currentBufferId++;
    }

    public static void put(int bufferId, byte position, Text value) {
        MultilineMessageBuffer buffer = MESSAGE_BUFFERS.get(bufferId);
        if (buffer != null) {
            buffer.put(position, value);
        }
    }

    private final int bufferId;
    private final Text[] messages;
    private final CommandOutput target;

    private MultilineMessageBuffer(int bufferId, byte expectedMessageCount, CommandOutput target) {
        this.bufferId = bufferId;
        this.messages = new Text[expectedMessageCount];
        this.target = target;
    }

    private void put(byte position, Text value) {
        messages[position] = value;
        if (isBufferFull()) {
            sendBufferedMessages();
            cleanup();
        }
    }

    private boolean isBufferFull() {
        return !ArrayUtils.contains(messages, null);
    }

    private void sendBufferedMessages() {
        SynchronizedMessageQueue.queueMessages(target, messages);
    }

    private void cleanup() {
        MESSAGE_BUFFERS.remove(bufferId);
    }
}
