package dev.the_fireplace.lib.api.chat.injectables;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface TextPaginator
{
    void sendPaginatedChat(CommandSourceStack targetCommandSource, String switchPageCommand, List<? extends Component> allItems, int pageIndex);
}
