package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClothOptionDependency<S, T>
{
    AbstractConfigListEntry<T> getEntry();

    OptionTypeConverter<S, T> getConverter();

    S getConvertedValue();
}
