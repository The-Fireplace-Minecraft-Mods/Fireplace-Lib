package dev.the_fireplace.lib.impl.commandhelpers;

import dev.the_fireplace.lib.api.command.Requirements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public final class RequirementsImpl implements Requirements {
    @Deprecated
    public static final Requirements INSTANCE = new RequirementsImpl();

    private RequirementsImpl(){}

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
