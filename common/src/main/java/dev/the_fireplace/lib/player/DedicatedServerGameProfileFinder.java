package dev.the_fireplace.lib.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.player.injectables.GameProfileFinder;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import net.minecraft.server.players.ProfileResolver;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Implementation(environment = "SERVER")
public final class DedicatedServerGameProfileFinder implements GameProfileFinder
{
    private final EmptyUUID emptyUUID;
    private final Supplier<ProfileResolver> profileResolverSupplier;
    private final Supplier<MinecraftSessionService> sessionServiceSupplier;
    private final Set<UUID> uuidsWithoutProfiles = new HashSet<>();
    private final Set<String> namesWithoutProfiles = new HashSet<>();

    @Inject
    public DedicatedServerGameProfileFinder(EmptyUUID emptyUUID) {
        this.emptyUUID = emptyUUID;
        profileResolverSupplier = () -> FireplaceLibConstants.getServer().services().profileResolver();
        sessionServiceSupplier = () -> FireplaceLibConstants.getServer().services().sessionService();
    }

    @Override
    public Optional<GameProfile> findProfile(UUID playerId) {
        if (emptyUUID.is(playerId)) {
            return Optional.empty();
        }
        if (uuidsWithoutProfiles.contains(playerId)) {
            return Optional.empty();
        }
        Optional<GameProfile> cachedProfile = profileResolverSupplier.get().fetchById(playerId);
        if (cachedProfile.isPresent()) {
            return cachedProfile;
        }
        //TODO The 1.21.9+ profile resolver may be doing this automatically, making this redundant.
        ProfileResult profileResult = sessionServiceSupplier.get().fetchProfile(playerId, false);
        GameProfile profile = profileResult != null ? profileResult.profile() : null;
        if (profile == null || profile.name().isEmpty()) {
            uuidsWithoutProfiles.add(playerId);
            return Optional.empty();
        } else {
            //profileResolverSupplier.get().add(profile);
            return Optional.of(profile);
        }
    }

    @Override
    public Optional<GameProfile> findProfile(String playerName) {
        if (namesWithoutProfiles.contains(playerName) || playerName.isEmpty()) {
            return Optional.empty();
        }
        Optional<GameProfile> profile = profileResolverSupplier.get().fetchByName(playerName);
        if (profile.isEmpty()) {
            namesWithoutProfiles.add(playerName);
        }
        return profile;
    }
}
