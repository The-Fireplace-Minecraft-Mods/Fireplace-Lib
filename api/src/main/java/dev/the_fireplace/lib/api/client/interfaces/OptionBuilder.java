package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Client side only.
 */
public interface OptionBuilder<S>
{
    OptionBuilder<S> setDescriptionRowCount(byte descriptionRowCount);

    OptionBuilder<S> appendCustomDescriptionRow(Component customRow);

    OptionBuilder<S> setErrorSupplier(Function<S, Optional<Component>> errorSupplier);

    OptionBuilder<S> addDependency(OptionBuilder<Boolean> parent);

    <U> OptionBuilder<S> addDependency(OptionBuilder<U> parent, Predicate<U> dependencyMet);
}
