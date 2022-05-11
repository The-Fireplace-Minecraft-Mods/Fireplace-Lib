package dev.the_fireplace.lib.config.dummy.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class DummyOptionBuilder<S> implements OptionBuilder<S>
{
    @Override
    public OptionBuilder<S> setDescriptionRowCount(byte descriptionRowCount) {
        return this;
    }

    @Override
    public OptionBuilder<S> appendCustomDescriptionRow(Component customRow) {
        return this;
    }

    @Override
    public OptionBuilder<S> setErrorSupplier(Function<S, Optional<Component>> errorSupplier) {
        return this;
    }

    @Override
    public OptionBuilder<S> addDependency(OptionBuilder<Boolean> parent) {
        return this;
    }

    @Override
    public <U> OptionBuilder<S> addDependency(OptionBuilder<U> parent, Predicate<U> dependencyMet) {
        return this;
    }
}
