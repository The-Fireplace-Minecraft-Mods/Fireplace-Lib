package dev.the_fireplace.lib.command.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.the_fireplace.lib.api.command.interfaces.PlayerSelector;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

final class OfflinePlayerSelector implements PlayerSelector
{
    private final EntitySelector entitySelector;
    private final String offlinePlayerName;

    OfflinePlayerSelector(EntitySelector entitySelector, String offlinePlayerName) {
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
