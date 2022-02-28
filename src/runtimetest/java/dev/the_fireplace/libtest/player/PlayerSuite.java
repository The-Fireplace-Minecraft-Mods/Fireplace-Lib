package dev.the_fireplace.libtest.player;

import dev.the_fireplace.libtest.setup.TestSuite;

import javax.inject.Inject;

public final class PlayerSuite implements TestSuite
{
    private final GameProfileFinderTest gameProfileFinderTest;

    @Inject
    public PlayerSuite(GameProfileFinderTest gameProfileFinderTest) {
        this.gameProfileFinderTest = gameProfileFinderTest;
    }

    @Override
    public void execute() {
        this.gameProfileFinderTest.execute();
    }
}
