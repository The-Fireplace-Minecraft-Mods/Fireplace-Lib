package the_fireplace.lib.api.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import the_fireplace.lib.impl.chat.MessageQueueImpl;

public interface MessageQueue {
    static MessageQueue getInstance() {
        //noinspection deprecation
        return MessageQueueImpl.INSTANCE;
    }
    void queueMessages(CommandOutput messageTarget, Text... messages);
}
