package dev.the_fireplace.lib.config.dummy.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.DropdownOptionBuilder;

public class DummyDropdownOptionBuilder<S> extends DummyOptionBuilder<S> implements DropdownOptionBuilder<S>
{
    @Override
    public DropdownOptionBuilder<S> enableSuggestionMode() {
        return this;
    }
}
