package dev.the_fireplace.lib.api.client.interfaces;

public interface NumericOptionBuilder<S> extends OptionBuilder<S> {
    NumericOptionBuilder<S> setMinimum(S minimum);

    NumericOptionBuilder<S> setMaximum(S maximum);
}
