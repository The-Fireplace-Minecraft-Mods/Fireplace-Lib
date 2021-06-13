package dev.the_fireplace.lib.api.chat;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

public interface MessageQueue {
    void queueMessages(CommandOutput messageTarget, Text... messages);
}
