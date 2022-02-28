package dev.the_fireplace.libtest.player;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.libtest.setup.TestSuite;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;

public final class PlayerSuite extends TestSuite
{
    private final GameProfileFinderTest gameProfileFinderTest;

    @Inject
    public PlayerSuite(GameProfileFinderTest gameProfileFinderTest) {
        this.gameProfileFinderTest = gameProfileFinderTest;
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> commandContext) {
        this.gameProfileFinderTest.execute();
    }
}
