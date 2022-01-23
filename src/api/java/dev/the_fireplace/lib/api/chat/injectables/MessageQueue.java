package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MessageQueue
{
    /**
     * Synchronized message queue for sending single/multiline messages to help prevent overlap when multiple threads are sending messages at once.
     */
    void queueMessages(CommandOutput messageTarget, Text... messages);
}
