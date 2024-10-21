package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.the_fireplace.lib.api.command.interfaces.PlayerSelector;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

public final class OfflinePlayerArgumentParser
{
    public PlayerSelector parse(StringReader reader) throws CommandSyntaxException {
        int startCursor = reader.getCursor();
        EntitySelectorParser entitySelectorReader = new EntitySelectorParser(reader, true);
        EntitySelector entitySelector = entitySelectorReader.parse();
        if (entitySelector.getMaxResults() > 1) {
            reader.setCursor(0);
            throw EntityArgument.ERROR_NOT_SINGLE_PLAYER.createWithContext(reader);
        } else if (entitySelector.includesEntities() && !entitySelector.isSelfSelector()) {
            reader.setCursor(0);
            throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(reader);
        }

        int endCursor = reader.getCursor();
        reader.setCursor(startCursor);
        StringBuilder lastArgumentStringBuilder = new StringBuilder();
        while (reader.getCursor() < endCursor) {
            lastArgumentStringBuilder.append(reader.read());
        }
        return new OfflinePlayerSelector(entitySelector, lastArgumentStringBuilder.toString());
    }
}
