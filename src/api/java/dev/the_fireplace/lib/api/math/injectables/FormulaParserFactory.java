package dev.the_fireplace.lib.api.math.injectables;

import dev.the_fireplace.lib.api.math.interfaces.FormulaParser;

public interface FormulaParserFactory
{
    /**
     * Create a parser for the given formula.
     * Accepts a-z as variables, allowed operators are + - * / ^ ()
     * Other values will be stripped out.
     */
    FormulaParser createParser(String formula);
}
