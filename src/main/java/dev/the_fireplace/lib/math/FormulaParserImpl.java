package dev.the_fireplace.lib.math;

import dev.the_fireplace.lib.api.math.exceptions.ParsingException;
import dev.the_fireplace.lib.api.math.injectables.FormulaParserFactory;
import dev.the_fireplace.lib.api.math.interfaces.FormulaParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class FormulaParserImpl implements FormulaParser
{
    public static final Pattern VARIABLE_MATCHER = Pattern.compile("[a-zA-Z]");
    private final Map<Character, String> variables = new HashMap<>();
    private final FormulaParserFactory formulaParserFactory;
    private final String formula;

    public FormulaParserImpl(FormulaParserFactory formulaParserFactory, String formula) {
        this.formulaParserFactory = formulaParserFactory;
        this.formula = formula;
    }

    @Override
    public FormulaParser setVariable(char variable, String formulaValue) {
        variables.put(variable, formulaValue);
        return this;
    }

    @Override
    public FormulaParser setVariable(char variable, double value) {
        variables.put(variable, String.valueOf(value));
        return this;
    }

    @Override
    public double parseDouble() throws ParsingException {
        Map<Character, Double> variableValues = parseVariables();
        //noinspection RegExpRedundantEscape
        String formula = this.formula.replaceAll("[^a-zA-Z\\.\\+\\-\\*\\/\\(\\)\\^0-9]", "");
        //Deal with multiplication that doesn't use a sign
        formula = formula.replaceAll("([0-9a-zA-Z])([a-zA-Z])|([a-zA-Z])([0-9a-zA-Z])", "\\1*\\2");
        for (Map.Entry<Character, Double> entry : variableValues.entrySet()) {
            formula = formula.replaceAll(entry.getKey().toString(), String.valueOf(entry.getValue()));
        }
        if (VARIABLE_MATCHER.matcher(formula).find()) {
            throw new ParsingException("Undefined variable detected in formula! Currently parsed down to: " + formula);
        }
        return new MathParser(formula).parse();
    }

    private Map<Character, Double> parseVariables() throws ParsingException {
        Map<Character, Double> variableValues = new HashMap<>(this.variables.size());
        for (Map.Entry<Character, String> entry : this.variables.entrySet()) {
            FormulaParser valueParser = formulaParserFactory.createParser(entry.getValue());
            this.variables.forEach((variable, value) -> {
                if (variable == entry.getKey()) {
                    return;
                }
                valueParser.setVariable(variable, value);
            });
            variableValues.put(entry.getKey(), valueParser.parseDouble());
        }

        return variableValues;
    }

}
