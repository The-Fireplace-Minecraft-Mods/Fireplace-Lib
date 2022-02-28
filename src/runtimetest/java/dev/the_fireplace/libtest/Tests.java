package dev.the_fireplace.libtest;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.libtest.player.PlayerSuite;
import dev.the_fireplace.libtest.setup.TestFailedError;
import dev.the_fireplace.libtest.setup.TestSuite;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public final class Tests
{
    private final Map<String, TestSuite> testSuites = new HashMap<>();

    @Inject
    public Tests(PlayerSuite playerSuite) {
        this.testSuites.put("player", playerSuite);
    }

    public String execute() {
        try {
            this.testSuites.values().forEach(TestSuite::execute);
        } catch (TestFailedError testFailedError) {
            FireplaceLibConstants.getLogger().error(testFailedError);
            throw testFailedError;
        }
        return "All tests passed.";
    }

    public String execute(String suiteName) {
        if (!testSuites.containsKey(suiteName)) {
            throw new IllegalArgumentException("Invalid test suite name: " + suiteName);
        }
        try {
            this.testSuites.get(suiteName).execute();
        } catch (TestFailedError testFailedError) {
            FireplaceLibConstants.getLogger().error(testFailedError);
            throw testFailedError;
        }
        return "All " + suiteName + " tests passed.";
    }
}
