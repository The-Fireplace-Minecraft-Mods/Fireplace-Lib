package dev.the_fireplace.lib.api.chat;

import dev.the_fireplace.lib.impl.chat.TextPaginatorImpl;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;

public interface TextPaginator {
    static TextPaginator getInstance() {
        //noinspection deprecation
        return TextPaginatorImpl.INSTANCE;
    }
    void sendPaginatedChat(ServerCommandSource targetCommandSource, String switchPageCommand, List<? extends Text> allItems, int pageIndex);
}
