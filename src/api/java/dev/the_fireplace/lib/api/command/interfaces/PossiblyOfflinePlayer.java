package dev.the_fireplace.lib.api.command.interfaces;

import net.minecraft.server.network.ServerPlayerEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface PossiblyOfflinePlayer
{
    UUID getId();

    String getName();

    @Nullable
    ServerPlayerEntity entity();
}
