package the_fireplace.lib.api.command;

import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.lib.impl.command.RequirementsImpl;

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
