package dev.the_fireplace.lib.api.command.injectables;

import net.minecraft.server.command.ServerCommandSource;

public interface Requirements
{
    boolean entity(ServerCommandSource commandSource);

    boolean player(ServerCommandSource commandSource);

    boolean manageGameSettings(ServerCommandSource commandSource);

    boolean managePlayerAccess(ServerCommandSource commandSource);

    boolean manageServer(ServerCommandSource commandSource);
}
