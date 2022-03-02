package dev.the_fireplace.libtest.chat;

import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.injectables.MultilineMessageBuffer;
import dev.the_fireplace.libtest.setup.LoggerStub;
import dev.the_fireplace.libtest.setup.TestFailedError;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.LiteralText;

import javax.inject.Inject;

public final class MultilineMessageBufferTest
{
    private static final int INVALID_BUFFER_ID = -1;
    private final MultilineMessageBuffer multilineMessageBuffer;

    @Inject
    public MultilineMessageBufferTest(MultilineMessageBuffer multilineMessageBuffer) {
        this.multilineMessageBuffer = multilineMessageBuffer;
    }

    public void execute(CommandOutput commandOutput) {
        test_put_invalidBuffer_logsWarning();
        test_create_put_happyPath_noExceptions(commandOutput);
    }

    private void test_put_invalidBuffer_logsWarning() {
        LoggerStub logger = new LoggerStub();
        FireplaceLibConstants.setLogger(logger);

        this.multilineMessageBuffer.put(INVALID_BUFFER_ID, (byte) 0, new LiteralText(""));

        if (!logger.hasWarned()) {
            throw new TestFailedError("No warning about invalid buffer!");
        }
    }

    private void test_create_put_happyPath_noExceptions(CommandOutput commandOutput) {
        LoggerStub logger = new LoggerStub();
        FireplaceLibConstants.setLogger(logger);

        int bufferId = this.multilineMessageBuffer.create((byte) 2, commandOutput);
        this.multilineMessageBuffer.put(bufferId, (byte) 1, new LiteralText("Multiline Message Buffer 2"));
        this.multilineMessageBuffer.put(bufferId, (byte) 0, new LiteralText("Multiline Message Buffer 1"));

        if (logger.hasWarned() || logger.hasErrored()) {
            throw new TestFailedError("Warning/error about valid buffer!");
        }
    }
}
