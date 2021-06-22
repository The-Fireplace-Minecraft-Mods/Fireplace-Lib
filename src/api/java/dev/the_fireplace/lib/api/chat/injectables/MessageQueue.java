package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MessageQueue {
    void queueMessages(CommandOutput messageTarget, Text... messages);
}
