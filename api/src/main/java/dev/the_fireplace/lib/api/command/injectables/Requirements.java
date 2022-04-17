package dev.the_fireplace.lib.api.command.injectables;

import net.minecraft.commands.CommandSourceStack;

public interface Requirements
{
    boolean entity(CommandSourceStack commandSource);

    boolean player(CommandSourceStack commandSource);

    boolean manageGameSettings(CommandSourceStack commandSource);

    boolean managePlayerAccess(CommandSourceStack commandSource);

    boolean manageServer(CommandSourceStack commandSource);
}
