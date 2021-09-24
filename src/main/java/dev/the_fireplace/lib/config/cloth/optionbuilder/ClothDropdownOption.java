package dev.the_fireplace.lib.config.cloth.optionbuilder;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.DropdownOptionBuilder;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ClothDropdownOption<S, T> extends ClothGenericOption<S, T> implements DropdownOptionBuilder<S> {
    public ClothDropdownOption(Translator translator, FieldBuilder<S, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction);
    }

    public ClothDropdownOption(Translator translator, FieldBuilder<T, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction, OptionTypeConverter<S, T> typeConverter) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, typeConverter);
    }

    @Override
    public DropdownOptionBuilder<S> enableSuggestionMode() {
        try {
            Method setMaximum = findSingleParameterMethod("setSuggestionMode", Boolean.class);
            setMaximum.invoke(fieldBuilder, true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set suggestion mode for field builder of type " + fieldBuilder.getClass(), e);
            FireplaceLib.getLogger().trace(ArrayUtils.toString(fieldBuilder.getClass().getMethods()));
        }
        return this;
    }
}
