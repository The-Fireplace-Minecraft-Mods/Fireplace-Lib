package dev.the_fireplace.lib.math;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.math.injectables.FormulaParserFactory;
import dev.the_fireplace.lib.api.math.interfaces.FormulaParser;

@Implementation
public final class FormulaParserFactoryImpl implements FormulaParserFactory
{
    @Override
    public FormulaParser createParser(String formula) {
        return new FormulaParserImpl(this, formula);
    }
}
