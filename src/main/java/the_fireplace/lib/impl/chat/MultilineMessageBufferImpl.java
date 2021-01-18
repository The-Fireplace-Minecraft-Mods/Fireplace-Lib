package the_fireplace.lib.impl.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import the_fireplace.lib.api.chat.MessageQueue;
import the_fireplace.lib.api.chat.MultilineMessageBuffer;
import the_fireplace.lib.impl.FireplaceLib;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public final class MultilineMessageBufferImpl implements MultilineMessageBuffer {
    @Deprecated
    public static final MultilineMessageBuffer INSTANCE = new MultilineMessageBufferImpl();
    private final Map<Integer, Buffer> messageBuffers = new ConcurrentHashMap<>();
    private final AtomicInteger currentBufferId = new AtomicInteger(Integer.MIN_VALUE);

    @Override
    public int create(byte expectedMessageCount, CommandOutput target) {
        int bufferId = currentBufferId.getAndIncrement();
        messageBuffers.put(bufferId, new Buffer(bufferId, expectedMessageCount, target));
        return bufferId;
    }

    @Override
    public void put(int bufferId, byte position, Text value) {
        Buffer buffer = messageBuffers.get(bufferId);
        if (buffer != null) {
            buffer.put(position, value);
        } else {
            FireplaceLib.getLogger().warn("Tried to add a message to nonexistant buffer "+bufferId+"!", new Exception("Stack trace"));
        }
    }

    private class Buffer {
        private final int bufferId;
        private final Text[] messages;
        private final CommandOutput target;

        private Buffer(int bufferId, byte expectedMessageCount, CommandOutput target) {
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
            MessageQueue.getInstance().queueMessages(target, messages);
        }

        private void cleanup() {
            messageBuffers.remove(bufferId);
        }
    }
}
