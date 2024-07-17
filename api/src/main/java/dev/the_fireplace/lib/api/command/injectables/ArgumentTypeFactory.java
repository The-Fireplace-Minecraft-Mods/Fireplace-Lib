package dev.the_fireplace.lib.api.command.injectables;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public interface ArgumentTypeFactory
{
    StringArgumentType possiblyOfflinePlayer();

    PossiblyOfflinePlayer getPossiblyOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException;

    <S> CompletableFuture<Suggestions> listOfflinePlayerSuggestions(CommandContext<S> context, SuggestionsBuilder builder);
}
