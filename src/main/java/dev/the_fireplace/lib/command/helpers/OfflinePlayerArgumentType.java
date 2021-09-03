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
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.UserCache;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class OfflinePlayerArgumentType implements OfflineSupportedPlayerArgumentType {
    private static final Collection<String> EXAMPLES = Arrays.asList("PlayerName", "@p", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    @Override
    public PlayerSelector parse(StringReader reader) throws CommandSyntaxException {
        int startCursor = reader.getCursor();
        EntitySelectorReader entitySelectorReader = new EntitySelectorReader(reader);
        EntitySelector entitySelector = entitySelectorReader.read();
        if (entitySelector.getLimit() > 1) {
            reader.setCursor(0);
            throw EntityArgumentType.TOO_MANY_PLAYERS_EXCEPTION.createWithContext(reader);
        } else if (entitySelector.includesNonPlayers() && !entitySelector.isSenderOnly()) {
            reader.setCursor(0);
            throw EntityArgumentType.PLAYER_SELECTOR_HAS_ENTITIES_EXCEPTION.createWithContext(reader);
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
        if (context.getSource() instanceof CommandSource commandSource) {
            StringReader reader = new StringReader(builder.getInput());
            reader.setCursor(builder.getStart());
            EntitySelectorReader entitySelectorReader = new EntitySelectorReader(reader);

            try {
                entitySelectorReader.read();
            } catch (CommandSyntaxException ignored) {}

            return entitySelectorReader.listSuggestions(builder, (suggestionsBuilder) -> {
                Iterable<String> iterable = commandSource.getPlayerNames();
                CommandSource.suggestMatching(iterable, suggestionsBuilder);
            });
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }


    private static class PlayerSelector implements dev.the_fireplace.lib.api.command.interfaces.PlayerSelector {

        private final EntitySelector entitySelector;
        private final String offlinePlayerName;

        private PlayerSelector(EntitySelector entitySelector, String offlinePlayerName) {
            this.entitySelector = entitySelector;
            this.offlinePlayerName = offlinePlayerName;
        }

        @Override
        public PossiblyOfflinePlayer get(ServerCommandSource source) throws CommandSyntaxException {
            try {
                List<ServerPlayerEntity> list = entitySelector.getPlayers(source);
                if (list.size() != 1) {
                    throw EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION.create();
                }
                ServerPlayerEntity player = list.get(0);

                return new SelectedPlayerArgument(player.getGameProfile(), player);
            } catch (CommandSyntaxException e) {
                MinecraftServer server = source.getServer();
                UserCache.setUseRemote(true);
                Optional<GameProfile> offlinePlayerProfileByName = server.getUserCache().findByName(offlinePlayerName);
                if (offlinePlayerProfileByName.isPresent()) {
                    return new SelectedPlayerArgument(offlinePlayerProfileByName.get());
                }
                try {
                    Optional<GameProfile> offlinePlayerProfileById = server.getUserCache().getByUuid(UUID.fromString(offlinePlayerName));
                    if (offlinePlayerProfileById.isPresent()) {
                        return new SelectedPlayerArgument(offlinePlayerProfileById.get());
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }

            throw EntityArgumentType.PLAYER_NOT_FOUND_EXCEPTION.create();
        }
    }

    /**
     * Serialize like {@link EntityArgumentType.Serializer} so we can mimic it when sending to a client that doesn't have FL installed.
     */
    public static class Serializer implements ArgumentSerializer<OfflinePlayerArgumentType> {
        @Override
        public void toPacket(OfflinePlayerArgumentType entityArgumentType, PacketByteBuf packetByteBuf) {
            byte b = 0;
            b = (byte) (b | 1);

            b = (byte) (b | 2);

            packetByteBuf.writeByte(b);
        }

        @Override
        public OfflinePlayerArgumentType fromPacket(PacketByteBuf packetByteBuf) {
            return new OfflinePlayerArgumentType();
        }

        @Override
        public void toJson(OfflinePlayerArgumentType entityArgumentType, JsonObject jsonObject) {
            jsonObject.addProperty("amount", "single");
            jsonObject.addProperty("type", "players");
        }
    }
}
