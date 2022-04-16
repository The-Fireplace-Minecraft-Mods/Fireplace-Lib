package dev.the_fireplace.lib.api.client.interfaces;

/**
 * Client side only
 */
public interface DropdownOptionBuilder<S> extends OptionBuilder<S>
{
    DropdownOptionBuilder<S> enableSuggestionMode();
}
