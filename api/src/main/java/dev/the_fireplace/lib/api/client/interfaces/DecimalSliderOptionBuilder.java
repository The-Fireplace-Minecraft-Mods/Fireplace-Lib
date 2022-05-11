package dev.the_fireplace.lib.api.client.interfaces;

/**
 * Client side only
 */
public interface DecimalSliderOptionBuilder<S> extends NumericOptionBuilder<S>
{
    DecimalSliderOptionBuilder<S> setPrecision(byte precision);

    DecimalSliderOptionBuilder<S> enablePercentMode();
}
