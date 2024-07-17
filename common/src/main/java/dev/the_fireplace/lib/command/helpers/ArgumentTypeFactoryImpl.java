package dev.the_fireplace.lib.command.helpers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.ArgumentTypeFactory;
import dev.the_fireplace.lib.api.command.interfaces.PlayerSelector;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Implementation
@Singleton
public final class ArgumentTypeFactoryImpl implements ArgumentTypeFactory
{
    @Override
    public StringArgumentType possiblyOfflinePlayer() {
        return StringArgumentType.word();
    }

    @Override
    public PossiblyOfflinePlayer getPossiblyOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        OfflinePlayerArgumentParser offlinePlayerArgumentParser = new OfflinePlayerArgumentParser();
        String inputString = StringArgumentType.getString(commandContext, string);
        PlayerSelector selector = offlinePlayerArgumentParser.parse(new StringReader(inputString));
        return selector.get(commandContext.getSource());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listOfflinePlayerSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider commandSource) {
            StringReader reader = new StringReader(builder.getInput());
            reader.setCursor(builder.getStart());
            EntitySelectorParser entitySelectorReader = new EntitySelectorParser(reader);

            try {
                entitySelectorReader.parse();
            } catch (CommandSyntaxException ignored) {
            }

            return entitySelectorReader.fillSuggestions(builder, (suggestionsBuilder) -> {
                Iterable<String> iterable = commandSource.getOnlinePlayerNames();
                SharedSuggestionProvider.suggest(iterable, suggestionsBuilder);
            });
        } else {
            return Suggestions.empty();
        }
    }
}
