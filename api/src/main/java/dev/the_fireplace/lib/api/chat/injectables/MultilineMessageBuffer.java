package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

public interface MultilineMessageBuffer
{
    /**
     * Create a new message buffer.
     *
     * @return The id of the newly created buffer
     */
    int create(byte expectedMessageCount, CommandSource target);

    /**
     * Add a message to the buffer with the given id.
     */
    void put(int bufferId, byte position, Component value);
}
