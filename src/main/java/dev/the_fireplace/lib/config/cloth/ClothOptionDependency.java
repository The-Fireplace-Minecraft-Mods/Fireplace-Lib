package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;

public interface ClothOptionDependency<S, T> {
    AbstractConfigListEntry<T> getEntry();

    OptionTypeConverter<S, T> getConverter();

    S getConvertedValue();
}
