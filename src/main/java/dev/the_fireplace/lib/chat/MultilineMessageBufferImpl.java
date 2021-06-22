package dev.the_fireplace.lib.chat;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLib;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.MultilineMessageBuffer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Implementation
@Singleton
public final class MultilineMessageBufferImpl implements MultilineMessageBuffer {
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
        private MessageQueue messageQueue;

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
            messageQueue.queueMessages(target, messages);
        }

        private void cleanup() {
            messageBuffers.remove(bufferId);
        }

        @Inject
        public void setMessageQueue(MessageQueue messageQueue) {
            this.messageQueue = messageQueue;
        }
    }
}
