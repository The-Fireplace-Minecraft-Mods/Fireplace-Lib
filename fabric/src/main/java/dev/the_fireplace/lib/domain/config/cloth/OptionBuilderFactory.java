package dev.the_fireplace.lib.domain.config.cloth;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.DecimalSliderOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.DropdownOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.NumericOptionBuilder;
import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface OptionBuilderFactory
{
    <S> OptionBuilder<S> create(
        Translator translator,
        FieldBuilder<S, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction
    );

    <S, T> OptionBuilder<S> create(
        Translator translator,
        FieldBuilder<T, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        OptionTypeConverter<S, T> typeConverter
    );

    <S> NumericOptionBuilder<S> createNumeric(
        Translator translator,
        FieldBuilder<S, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction
    );

    <S, T> NumericOptionBuilder<S> createNumeric(
        Translator translator,
        FieldBuilder<T, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        OptionTypeConverter<S, T> typeConverter
    );

    <S, T> DecimalSliderOptionBuilder<S> createDecimalSlider(
        Translator translator,
        FieldBuilder<T, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Consumer<S> saveFunction,
        S currentValue,
        S minimum,
        S maximum,
        OptionTypeConverter<S, T> typeConverter
    );

    <S> DropdownOptionBuilder<S> createDropdown(
        Translator translator,
        FieldBuilder<S, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Iterable<S> entries,
        Consumer<S> saveFunction
    );

    <S, T> DropdownOptionBuilder<S> createDropdown(
        Translator translator,
        FieldBuilder<T, ?, ?> fieldBuilder,
        String optionTranslationBase,
        S defaultValue,
        Iterable<S> entries,
        Consumer<S> saveFunction,
        OptionTypeConverter<S, T> typeConverter
    );
}
