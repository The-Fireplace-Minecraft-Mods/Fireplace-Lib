package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MultilineMessageBuffer
{
    /**
     * Create a new message buffer.
     *
     * @return The id of the newly created buffer
     */
    int create(byte expectedMessageCount, CommandOutput target);

    /**
     * Add a message to the buffer with the given id.
     */
    void put(int bufferId, byte position, Text value);
}
