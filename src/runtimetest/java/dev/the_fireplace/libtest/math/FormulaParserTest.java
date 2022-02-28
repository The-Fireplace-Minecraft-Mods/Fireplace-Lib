package dev.the_fireplace.libtest.math;

import dev.the_fireplace.lib.api.math.exceptions.ParsingException;
import dev.the_fireplace.lib.api.math.injectables.FormulaParserFactory;
import dev.the_fireplace.lib.api.math.interfaces.FormulaParser;
import dev.the_fireplace.libtest.setup.TestFailedError;

import javax.inject.Inject;

public final class FormulaParserTest
{
    private final FormulaParserFactory formulaParserFactory;

    @Inject
    public FormulaParserTest(FormulaParserFactory formulaParserFactory) {
        this.formulaParserFactory = formulaParserFactory;
    }

    public void execute() {
        test_parse_simpleAddition_addsNumbers();
        test_parse_simpleSubtraction_subtractsNumbers();
        test_parse_multiplication_multipliesNumbers();
        test_parse_division_dividesNumbers();
        test_parse_exponent_exponentiatesNumbers();
        test_parse_variable_usesVariableValue();
        test_parse_nestedVariable_usesParsedFormulaValue();
        test_parse_missingVariable_throwsException();
        test_parse_missingNestedVariable_throwsException();
        test_parse_circularVariable_throwsException();
        test_parse_orderOfOperations_returnsCorrectResult();
    }

    private boolean isUnequal(double expected, double actual) {
        double difference = expected - actual;
        return !(difference < 0.000000001) || !(difference > -0.000000001);
    }

    private void test_parse_simpleAddition_addsNumbers() {
        String formula = "1 + 2 + 3";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if ((int) result != 6) {
            throw new TestFailedError("Formula parser: simple addition failed!");
        }
    }

    private void test_parse_simpleSubtraction_subtractsNumbers() {
        String formula = "1 - 2 - 3";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if ((int) result != -4) {
            throw new TestFailedError("Formula parser: simple subtraction failed!");
        }
    }

    private void test_parse_multiplication_multipliesNumbers() {
        String formula = "1.5 * 8";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if (isUnequal(12, result)) {
            throw new TestFailedError("Formula parser: simple multiplication failed!");
        }
    }

    private void test_parse_division_dividesNumbers() {
        String formula = "9 / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if (isUnequal(4.5, result)) {
            throw new TestFailedError("Formula parser: simple division failed!");
        }
    }

    private void test_parse_exponent_exponentiatesNumbers() {
        String formula = "3 ^ 4";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if (isUnequal(81, result)) {
            throw new TestFailedError("Formula parser: exponentiation failed!");
        }
    }

    private void test_parse_variable_usesVariableValue() {
        String formula = "x / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        formulaParser.setVariable('x', 9);
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if (isUnequal(4.5, result)) {
            throw new TestFailedError("Formula parser: variable failed!");
        }
    }

    private void test_parse_nestedVariable_usesParsedFormulaValue() {
        String formula = "x / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        formulaParser.setVariable('x', "7 + 2");
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }
        if (isUnequal(4.5, result)) {
            throw new TestFailedError("Formula parser: variable with formula value failed!");
        }
    }

    private void test_parse_missingVariable_throwsException() {
        String formula = "x / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        try {
            formulaParser.parseDouble();
            throw new TestFailedError("Formula parser: parsed with missing variable!");
        } catch (ParsingException e) {
            // Pass
        }
    }

    private void test_parse_missingNestedVariable_throwsException() {
        String formula = "x / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        formulaParser.setVariable('x', "y + 2");
        try {
            formulaParser.parseDouble();
            throw new TestFailedError("Formula parser: parsed with missing variable!");
        } catch (ParsingException e) {
            // Pass
        }
    }

    private void test_parse_circularVariable_throwsException() {
        String formula = "x / 2";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        formulaParser.setVariable('x', "y + 2");
        formulaParser.setVariable('y', "z + 2");
        formulaParser.setVariable('z', "x + 2");
        try {
            formulaParser.parseDouble();
            throw new TestFailedError("Formula parser: parsed with missing variable!");
        } catch (ParsingException e) {
            // Pass
        }
    }

    private void test_parse_orderOfOperations_returnsCorrectResult() {
        String formula = "x / (3 ^ 2 - 7) - 4.2 - z ^ 0";
        FormulaParser formulaParser = formulaParserFactory.createParser(formula);
        formulaParser.setVariable('x', "y + z");
        formulaParser.setVariable('y', "z + 2.1");
        formulaParser.setVariable('z', "15");
        double result;
        try {
            result = formulaParser.parseDouble();
        } catch (ParsingException e) {
            throw new TestFailedError(e);
        }

        if (isUnequal(10.85, result)) {
            throw new TestFailedError("Order of operations test failed! Got " + result);
        }
    }
}
