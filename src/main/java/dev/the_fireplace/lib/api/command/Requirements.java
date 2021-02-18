package dev.the_fireplace.lib.api.command;

import dev.the_fireplace.lib.impl.commandhelpers.RequirementsImpl;
import net.minecraft.server.command.ServerCommandSource;

public interface Requirements {
	static Requirements getInstance() {
		//noinspection deprecation
		return RequirementsImpl.INSTANCE;
	}

	boolean entity(ServerCommandSource commandSource);

	boolean player(ServerCommandSource commandSource);

	boolean manageGameSettings(ServerCommandSource commandSource);

	boolean managePlayerAccess(ServerCommandSource commandSource);

	boolean manageServer(ServerCommandSource commandSource);
}
