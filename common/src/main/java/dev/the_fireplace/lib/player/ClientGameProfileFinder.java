package dev.the_fireplace.lib.player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.player.injectables.GameProfileFinder;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Implementation(environment = "CLIENT")
@Singleton
public final class ClientGameProfileFinder implements GameProfileFinder
{
    private final EmptyUUID emptyUUID;
    private final Minecraft client;
    private final Map<UUID, Optional<GameProfile>> profilesById = new ConcurrentHashMap<>();
    private final Map<String, Optional<GameProfile>> profilesByName = new ConcurrentHashMap<>();

    @Inject
    public ClientGameProfileFinder(EmptyUUID emptyUUID) {
        this.emptyUUID = emptyUUID;
        this.client = Minecraft.getInstance();
    }

    @Override
    public Optional<GameProfile> findProfile(UUID playerId) {
        if (emptyUUID.is(playerId)) {
            return Optional.empty();
        }
        if (profilesById.containsKey(playerId)) {
            return profilesById.get(playerId);
        }
        GameProfile profile = new GameProfile(playerId, "");
        profile = client.getMinecraftSessionService().fillProfileProperties(profile, false);
        Optional<GameProfile> wrappedProfile;
        if (profile.getName().isEmpty()) {
            wrappedProfile = Optional.empty();
        } else {
            wrappedProfile = Optional.of(profile);
            profilesByName.put(profile.getName(), wrappedProfile);
        }
        profilesById.put(playerId, wrappedProfile);
        return wrappedProfile;
    }

    @Override
    public Optional<GameProfile> findProfile(String playerName) {
        if (playerName.isEmpty()) {
            return Optional.empty();
        }
        if (profilesByName.containsKey(playerName)) {
            return profilesByName.get(playerName);
        }
        MinecraftServer server = client.getSingleplayerServer();
        if (server != null) {
            GameProfileCache.setUsesAuthentication(true);
            Optional<GameProfile> foundProfile = server.getProfileCache().get(playerName);
            profilesByName.put(playerName, foundProfile);
            return foundProfile;
        }
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            JsonObject object = new Gson().fromJson(new InputStreamReader(con.getInputStream()), JsonObject.class);
            con.disconnect();
            if (object.has("id")) {
                Optional<GameProfile> gameProfile = findProfile(UUID.fromString(object.get("id").getAsString()));
                profilesByName.put(playerName, gameProfile);
            } else {
                profilesByName.put(playerName, Optional.empty());
            }
        } catch (IOException e) {
            profilesByName.put(playerName, Optional.empty());
        }

        return profilesByName.get(playerName);
    }
}
