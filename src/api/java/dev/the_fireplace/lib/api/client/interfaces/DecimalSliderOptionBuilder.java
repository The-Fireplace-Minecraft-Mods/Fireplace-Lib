package dev.the_fireplace.lib.api.client.interfaces;

public interface DecimalSliderOptionBuilder<S> extends NumericOptionBuilder<S> {
    DecimalSliderOptionBuilder<S> setPrecision(byte precision);

    void enablePercentMode();
}
