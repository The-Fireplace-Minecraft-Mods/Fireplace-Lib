package dev.the_fireplace.libtest.math;

import com.mojang.brigadier.context.CommandContext;
import dev.the_fireplace.libtest.setup.TestSuite;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;

public final class MathSuite extends TestSuite
{
    private final FormulaParserTest formulaParserTest;

    @Inject
    public MathSuite(FormulaParserTest formulaParserTest) {
        this.formulaParserTest = formulaParserTest;
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> commandContext) {
        formulaParserTest.execute();
    }
}
