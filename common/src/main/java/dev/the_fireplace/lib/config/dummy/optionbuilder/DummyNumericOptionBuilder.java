package dev.the_fireplace.lib.config.dummy.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.NumericOptionBuilder;

public class DummyNumericOptionBuilder<S> extends DummyOptionBuilder<S> implements NumericOptionBuilder<S>
{
    @Override
    public NumericOptionBuilder<S> setMinimum(S minimum) {
        return this;
    }

    @Override
    public NumericOptionBuilder<S> setMaximum(S maximum) {
        return this;
    }
}
