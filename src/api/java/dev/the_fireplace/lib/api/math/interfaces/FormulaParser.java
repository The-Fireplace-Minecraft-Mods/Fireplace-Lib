package dev.the_fireplace.lib.api.math.interfaces;

import dev.the_fireplace.lib.api.math.exception.ParsingException;

public interface FormulaParser
{
    FormulaParser setVariable(char variable, String formulaValue);

    FormulaParser setVariable(char variable, double value);

    double parseDouble() throws ParsingException;
}
