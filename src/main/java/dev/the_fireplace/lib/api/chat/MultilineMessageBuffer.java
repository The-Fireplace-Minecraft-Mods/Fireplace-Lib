package dev.the_fireplace.lib.api.chat;

import dev.the_fireplace.lib.impl.chat.MultilineMessageBufferImpl;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MultilineMessageBuffer {
    static MultilineMessageBuffer getInstance() {
        //noinspection deprecation
        return MultilineMessageBufferImpl.INSTANCE;
    }

    /**
     * Create a new message buffer.
     * @param expectedMessageCount
     * @param target
     * @return
     * The id of the newly created buffer
     */
    int create(byte expectedMessageCount, CommandOutput target);

    /**
     * Add a message to the buffer with the given id.
     * @param bufferId
     * @param position
     * @param value
     */
    void put(int bufferId, byte position, Text value);
}
