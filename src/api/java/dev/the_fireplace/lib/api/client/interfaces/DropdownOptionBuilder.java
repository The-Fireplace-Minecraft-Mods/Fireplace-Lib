package dev.the_fireplace.lib.api.client.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface DropdownOptionBuilder<S> extends OptionBuilder<S> {
    DropdownOptionBuilder<S> enableSuggestionMode();
}
