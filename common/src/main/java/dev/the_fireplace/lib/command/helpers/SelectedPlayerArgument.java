package dev.the_fireplace.lib.command.helpers;

import com.mojang.authlib.GameProfile;
import dev.the_fireplace.lib.api.command.interfaces.PossiblyOfflinePlayer;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

public class SelectedPlayerArgument implements PossiblyOfflinePlayer
{

    private final GameProfile profile;
    @Nullable
    private final ServerPlayer serverPlayerEntity;

    public SelectedPlayerArgument(GameProfile profile) {
        this(profile, null);
    }

    public SelectedPlayerArgument(GameProfile profile, @Nullable ServerPlayer serverPlayerEntity) {
        this.profile = profile;
        this.serverPlayerEntity = serverPlayerEntity;
    }

    @Override
    public UUID getId() {
        return profile.getId();
    }

    @Override
    public String getName() {
        return profile.getName();
    }

    @Nullable
    @Override
    public ServerPlayer entity() {
        return serverPlayerEntity;
    }
}
