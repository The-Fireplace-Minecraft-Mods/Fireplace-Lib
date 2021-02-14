package dev.the_fireplace.lib.api.chat;

import dev.the_fireplace.lib.impl.chat.MessageQueueImpl;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MessageQueue {
    static MessageQueue getInstance() {
        //noinspection deprecation
        return MessageQueueImpl.INSTANCE;
    }
    void queueMessages(CommandOutput messageTarget, Text... messages);
}
