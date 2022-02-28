package dev.the_fireplace.libtest;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.libtest.chat.ChatSuite;
import dev.the_fireplace.libtest.player.PlayerSuite;
import dev.the_fireplace.libtest.setup.TestFailedError;
import dev.the_fireplace.libtest.setup.TestSuite;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public final class Tests
{
    private final Map<String, TestSuite> testSuites = new HashMap<>();
    private final Logger logger = LogManager.getLogger("Fireplace Lib Integration Tests");

    @Inject
    public Tests(ChatSuite chatSuite, PlayerSuite playerSuite) {
        this.testSuites.put("chat", chatSuite);
        this.testSuites.put("player", playerSuite);
    }

    private String executeAll(CommandContext<ServerCommandSource> commandContext) {
        try {
            this.testSuites.values().forEach(testSuite -> testSuite.execute(commandContext));
        } catch (TestFailedError testFailedError) {
            logger.error(testFailedError);
            throw testFailedError;
        }
        return "All tests passed.";
    }

    public String execute(CommandContext<ServerCommandSource> commandContext, String suiteName) {
        if (suiteName.equals("all")) {
            return executeAll(commandContext);
        }
        if (!testSuites.containsKey(suiteName)) {
            throw new IllegalArgumentException("Invalid test suite name: " + suiteName);
        }
        try {
            this.testSuites.get(suiteName).execute(commandContext);
        } catch (TestFailedError testFailedError) {
            logger.error(testFailedError);
            throw testFailedError;
        }
        return "All " + suiteName + " tests passed.";
    }
}
