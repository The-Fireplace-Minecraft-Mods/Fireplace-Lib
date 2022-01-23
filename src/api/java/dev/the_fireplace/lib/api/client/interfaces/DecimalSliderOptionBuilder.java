package dev.the_fireplace.lib.api.client.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DecimalSliderOptionBuilder<S> extends NumericOptionBuilder<S>
{
    DecimalSliderOptionBuilder<S> setPrecision(byte precision);

    void enablePercentMode();
}
