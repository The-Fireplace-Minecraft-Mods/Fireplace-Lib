package dev.the_fireplace.lib.api.player.injectables;

import com.mojang.authlib.GameProfile;

import java.util.Optional;
import java.util.UUID;

public interface GameProfileFinder
{
    Optional<GameProfile> findProfile(UUID playerId);

    Optional<GameProfile> findProfile(String playerName);
}
