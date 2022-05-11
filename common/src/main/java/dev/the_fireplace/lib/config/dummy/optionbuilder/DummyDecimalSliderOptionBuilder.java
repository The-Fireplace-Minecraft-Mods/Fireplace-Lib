package dev.the_fireplace.lib.config.dummy.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.DecimalSliderOptionBuilder;

public class DummyDecimalSliderOptionBuilder<S> extends DummyNumericOptionBuilder<S> implements DecimalSliderOptionBuilder<S>
{
    @Override
    public DecimalSliderOptionBuilder<S> setPrecision(byte precision) {
        return this;
    }

    @Override
    public DecimalSliderOptionBuilder<S> enablePercentMode() {
        return this;
    }
}
