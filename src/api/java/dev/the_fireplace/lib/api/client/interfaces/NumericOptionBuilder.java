package dev.the_fireplace.lib.api.client.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface NumericOptionBuilder<S> extends OptionBuilder<S>
{
    NumericOptionBuilder<S> setMinimum(S minimum);

    NumericOptionBuilder<S> setMaximum(S maximum);
}
