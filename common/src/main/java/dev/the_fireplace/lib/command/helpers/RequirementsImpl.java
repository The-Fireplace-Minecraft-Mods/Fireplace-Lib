package dev.the_fireplace.lib.command.helpers;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import javax.inject.Singleton;

@Implementation
@Singleton
public final class RequirementsImpl implements Requirements
{
    private static final int PERMISSION_LEVEL_MANAGE_GAME_SETTINGS = 2;
    private static final int PERMISSION_LEVEL_MANAGE_PLAYER_ACCESS = 3;
    private static final int PERMISSION_LEVEL_OP = 4;

    @Override
    public boolean entity(CommandSourceStack commandSource) {
        return commandSource.getEntity() != null;
    }

    @Override
    public boolean player(CommandSourceStack commandSource) {
        return commandSource.getEntity() instanceof Player;
    }

    @Override
    public boolean manageGameSettings(CommandSourceStack commandSource) {
        return commandSource.hasPermission(PERMISSION_LEVEL_MANAGE_GAME_SETTINGS);
    }

    @Override
    public boolean managePlayerAccess(CommandSourceStack commandSource) {
        return commandSource.hasPermission(PERMISSION_LEVEL_MANAGE_PLAYER_ACCESS);
    }

    @Override
    public boolean manageServer(CommandSourceStack commandSource) {
        return commandSource.hasPermission(PERMISSION_LEVEL_OP);
    }
}
