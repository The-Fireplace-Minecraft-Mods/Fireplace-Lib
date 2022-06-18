package dev.the_fireplace.lib.command.helpers;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.the_fireplace.lib.api.command.interfaces.OfflineSupportedPlayerArgumentType;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class OfflinePlayerArgumentType implements OfflineSupportedPlayerArgumentType
{
    private static final Collection<String> EXAMPLES = Arrays.asList("PlayerName", "@p", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    @Override
    public PlayerSelector parse(StringReader reader) throws CommandSyntaxException {
        int startCursor = reader.getCursor();
        EntitySelectorParser entitySelectorReader = new EntitySelectorParser(reader);
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
        return new PlayerSelector(entitySelector, lastArgumentStringBuilder.toString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider) {
            StringReader reader = new StringReader(builder.getInput());
            reader.setCursor(builder.getStart());
            EntitySelectorParser entitySelectorReader = new EntitySelectorParser(reader);

            try {
                entitySelectorReader.parse();
            } catch (CommandSyntaxException ignored) {
            }

            return entitySelectorReader.fillSuggestions(builder, (suggestionsBuilder) -> {
                Iterable<String> iterable = ((SharedSuggestionProvider) context.getSource()).getOnlinePlayerNames();
                SharedSuggestionProvider.suggest(iterable, suggestionsBuilder);
            });
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }


    private static class PlayerSelector implements dev.the_fireplace.lib.api.command.interfaces.PlayerSelector
    {

        private final EntitySelector entitySelector;
        private final String offlinePlayerName;

        private PlayerSelector(EntitySelector entitySelector, String offlinePlayerName) {
            this.entitySelector = entitySelector;
            this.offlinePlayerName = offlinePlayerName;
        }

        @Override
        public PossiblyOfflinePlayer get(CommandSourceStack source) throws CommandSyntaxException {
            try {
                List<ServerPlayer> list = entitySelector.findPlayers(source);
                if (list.size() != 1) {
                    throw EntityArgument.NO_PLAYERS_FOUND.create();
                }
                ServerPlayer player = list.get(0);

                return new SelectedPlayerArgument(player.getGameProfile(), player);
            } catch (CommandSyntaxException e) {
                MinecraftServer server = source.getServer();
                GameProfileCache.setUsesAuthentication(true);
                GameProfile offlinePlayerProfileByName = server.getProfileCache().get(offlinePlayerName);
                if (offlinePlayerProfileByName != null) {
                    return new SelectedPlayerArgument(offlinePlayerProfileByName);
                }
                try {
                    GameProfile offlinePlayerProfileById = server.getProfileCache().get(UUID.fromString(offlinePlayerName));
                    if (offlinePlayerProfileById != null) {
                        return new SelectedPlayerArgument(offlinePlayerProfileById);
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }

            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }
    }

    /**
     * Serialize like {@link EntityArgument.Serializer} so we can mimic it when sending to a client that doesn't have FL installed.
     */
    public static class Serializer implements ArgumentSerializer<OfflinePlayerArgumentType>
    {
        @Override
        public void serializeToNetwork(OfflinePlayerArgumentType entityArgumentType, FriendlyByteBuf packetByteBuf) {
            byte b = 0;
            b = (byte) (b | 1);

            b = (byte) (b | 2);

            packetByteBuf.writeByte(b);
        }

        @Override
        public OfflinePlayerArgumentType deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
            return new OfflinePlayerArgumentType();
        }

        @Override
        public void serializeToJson(OfflinePlayerArgumentType entityArgumentType, JsonObject jsonObject) {
            jsonObject.addProperty("amount", "single");
            jsonObject.addProperty("type", "players");
        }
    }
}
