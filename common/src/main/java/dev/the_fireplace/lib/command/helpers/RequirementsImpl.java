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
    @Override
    public boolean entity(CommandSourceStack commandSource) {
        return commandSource.getEntity() != null;
    }

    @Override
    public boolean player(CommandSourceStack commandSource) {
        return commandSource.getEntity() instanceof Player;
    }
}
