package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

public interface MessageQueue
{
    /**
     * Synchronized message queue for sending single/multiline messages to help prevent overlap when multiple threads are sending messages at once.
     */
    void queueMessages(CommandSource messageTarget, Component... messages);
}
