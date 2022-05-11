package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.DecimalSliderOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.DropdownOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.NumericOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothDecimalSliderOption;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothDropdownOption;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothGenericOption;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothNumericOption;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.domain.config.cloth.OptionBuilderFactory;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;

import java.util.function.Consumer;

@Implementation(environment = "CLIENT")
public final class OptionBuilderFactoryImpl implements OptionBuilderFactory
{
    @Override
    public <S> OptionBuilder<S> create(Translator translator, FieldBuilder<S, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction) {
        return new ClothGenericOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction);
    }

    @Override
    public <S, T> OptionBuilder<S> create(Translator translator, FieldBuilder<T, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction, OptionTypeConverter<S, T> typeConverter) {
        return new ClothGenericOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, typeConverter);
    }

    @Override
    public <S> NumericOptionBuilder<S> createNumeric(Translator translator, FieldBuilder<S, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction) {
        return new ClothNumericOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction);
    }

    @Override
    public <S, T> NumericOptionBuilder<S> createNumeric(Translator translator, FieldBuilder<T, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Consumer<S> saveFunction, OptionTypeConverter<S, T> typeConverter) {
        return new ClothNumericOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, typeConverter);
    }

    @Override
    public <S, T> DecimalSliderOptionBuilder<S> createDecimalSlider(
        Translator translator,
        FieldBuilder<T, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        S currentValue,
        S minimum,
        S maximum,
        OptionTypeConverter<S, T> typeConverter
    ) {
        return new ClothDecimalSliderOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction, currentValue, minimum, maximum, typeConverter);
    }

    @Override
    public <S> DropdownOptionBuilder<S> createDropdown(Translator translator, FieldBuilder<S, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Iterable<S> entries, Consumer<S> saveFunction) {
        return new ClothDropdownOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, entries, saveFunction);
    }

    @Override
    public <S, T> DropdownOptionBuilder<S> createDropdown(Translator translator, FieldBuilder<T, ?> fieldBuilder, String optionTranslationBase, S defaultValue, Iterable<S> entries, Consumer<S> saveFunction, OptionTypeConverter<S, T> typeConverter) {
        return new ClothDropdownOption<>(translator, fieldBuilder, optionTranslationBase, defaultValue, entries, saveFunction, typeConverter);
    }
}
