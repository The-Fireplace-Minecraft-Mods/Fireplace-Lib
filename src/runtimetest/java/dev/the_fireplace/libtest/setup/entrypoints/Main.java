package dev.the_fireplace.libtest.setup.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.entrypoints.DIModInitializer;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.libtest.setup.FLTestCommand;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class Main implements DIModInitializer
{
    @Override
    public void onInitialize(Injector diContainer) {
        FireplaceLibConstants.getLogger().info("Tests have initialized!");
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            FireplaceLibConstants.getLogger().info("Test command registering!");
            FLTestCommand flTestCommand = diContainer.getInstance(FLTestCommand.class);
            flTestCommand.register(server.getCommandManager().getDispatcher());
        });
    }
}
