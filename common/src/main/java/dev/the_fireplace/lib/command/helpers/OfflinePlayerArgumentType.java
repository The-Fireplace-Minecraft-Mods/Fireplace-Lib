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
import dev.the_fireplace.lib.command.helpers.OfflinePlayerArgumentType.Serializer.Template;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class OfflinePlayerArgumentType implements OfflineSupportedPlayerArgumentType
{
    private static final Collection<String> EXAMPLES = Arrays.asList("PlayerName", "@p", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    @Override
    public EntitySelector parse(StringReader reader) throws CommandSyntaxException {
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
        return null;//new PlayerSelector(entitySelector, lastArgumentStringBuilder.toString());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
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
                Optional<GameProfile> offlinePlayerProfileByName = server.getProfileCache().get(offlinePlayerName);
                if (offlinePlayerProfileByName.isPresent()) {
                    return new SelectedPlayerArgument(offlinePlayerProfileByName.get());
                }
                try {
                    Optional<GameProfile> offlinePlayerProfileById = server.getProfileCache().get(UUID.fromString(offlinePlayerName));
                    if (offlinePlayerProfileById.isPresent()) {
                        return new SelectedPlayerArgument(offlinePlayerProfileById.get());
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }

            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }
    }

    /**
     * Serialize like {@link EntityArgument.Info} so we can mimic it when sending to a client that doesn't have FL installed.
     */
    public static class Serializer implements ArgumentTypeInfo<OfflinePlayerArgumentType, Template>
    {
        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf packetByteBuf) {
            byte b = 0;
            b |= 1;
            b |= 2;

            packetByteBuf.writeByte(b);
        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf packetByteBuf) {
            return new Template();
        }

        @Override
        public void serializeToJson(Template template, JsonObject jsonObject) {
            jsonObject.addProperty("amount", "single");
            jsonObject.addProperty("type", "players");
        }

        @Override
        public Template unpack(OfflinePlayerArgumentType argumentType) {
            return new Template();
        }

        public final class Template implements ArgumentTypeInfo.Template<OfflinePlayerArgumentType>
        {

            @Override
            public OfflinePlayerArgumentType instantiate(CommandBuildContext buildContext) {
                return new OfflinePlayerArgumentType();
            }

            @Override
            public ArgumentTypeInfo<OfflinePlayerArgumentType, ?> type() {
                return OfflinePlayerArgumentType.Serializer.this;
            }
        }
    }
}
