package dev.the_fireplace.lib.impl.commandhelpers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class RequirementsImpl implements Requirements {
    private static final int PERMISSION_LEVEL_MANAGE_GAME_SETTINGS = 2;
    private static final int PERMISSION_LEVEL_MANAGE_PLAYER_ACCESS = 3;
    private static final int PERMISSION_LEVEL_OP = 4;

    @Override
    public boolean entity(ServerCommandSource commandSource) {
        return commandSource.getEntity() != null;
    }

    @Override
    public boolean player(ServerCommandSource commandSource) {
        return commandSource.getEntity() instanceof PlayerEntity;
    }

    @Override
    public boolean manageGameSettings(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PERMISSION_LEVEL_MANAGE_GAME_SETTINGS);
    }

    @Override
    public boolean managePlayerAccess(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PERMISSION_LEVEL_MANAGE_PLAYER_ACCESS);
    }

    @Override
    public boolean manageServer(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PERMISSION_LEVEL_OP);
    }
}
