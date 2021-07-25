package dev.the_fireplace.lib.api.client.interfaces;

import net.minecraft.client.gui.screen.Screen;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface ConfigScreenBuilder {
    ConfigScreenBuilder startCategory(String translationKey);

    ConfigScreenBuilder addStringField(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction
    );

    ConfigScreenBuilder addStringField(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addStringField(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction,
        byte descriptionRowCount,
        Function<String, Optional<String>> errorSupplier
    );

    <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(
        String optionTranslationBase,
        T currentValue,
        T defaultValue,
        Iterable<T> dropdownEntries,
        Consumer<T> saveFunction
    );

    <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(
        String optionTranslationBase,
        T currentValue,
        T defaultValue,
        Iterable<T> dropdownEntries,
        Consumer<T> saveFunction,
        byte descriptionRowCount
    );

    <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(
        String optionTranslationBase,
        T currentValue,
        T defaultValue,
        Iterable<T> dropdownEntries,
        Consumer<T> saveFunction,
        byte descriptionRowCount,
        Function<T, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction,
        boolean suggestionMode
    );

    ConfigScreenBuilder addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction,
        boolean suggestionMode,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction,
        boolean suggestionMode,
        byte descriptionRowCount,
        Function<String, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction
    );

    ConfigScreenBuilder addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction,
        byte descriptionRowCount,
        Function<List<String>, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction
    );

    ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max
    );

    ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount,
        Function<Float, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max
    );

    ConfigScreenBuilder addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount,
        Function<Float, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction
    );

    ConfigScreenBuilder addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction,
        byte descriptionRowCount,
        Function<List<Float>, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction
    );

    ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max
    );

    ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount,
        Function<Double, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max
    );

    ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount,
        byte precision
    );

    ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount,
        byte precision,
        Function<Double, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addDoublePercentSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction
    );

    ConfigScreenBuilder addDoublePercentSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        byte descriptionRowCount,
        byte precision
    );

    ConfigScreenBuilder addDoublePercentSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        byte descriptionRowCount,
        byte precision,
        Function<Double, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction
    );

    ConfigScreenBuilder addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction,
        byte descriptionRowCount,
        Function<List<Double>, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction
    );

    ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max
    );

    ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount,
        Function<Long, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max
    );

    ConfigScreenBuilder addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount,
        Function<Long, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction
    );

    ConfigScreenBuilder addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction,
        byte descriptionRowCount,
        Function<List<Long>, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction
    );

    ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max
    );

    ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount,
        Function<Integer, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max
    );

    ConfigScreenBuilder addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount,
        Function<Integer, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction
    );

    ConfigScreenBuilder addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction,
        byte descriptionRowCount,
        Function<List<Integer>, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction
    );

    ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max
    );

    ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount,
        Function<Short, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max
    );

    ConfigScreenBuilder addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount,
        Function<Short, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction
    );

    ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max
    );

    ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount,
        Function<Byte, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max
    );

    ConfigScreenBuilder addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount,
        Function<Byte, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction
    );

    ConfigScreenBuilder addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction,
        byte descriptionRowCount
    );

    ConfigScreenBuilder addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction,
        byte descriptionRowCount,
        Function<Boolean, Optional<String>> errorSupplier
    );

    ConfigScreenBuilder addOptionDependency(
        String parentTranslationBase,
        String childTranslationBase,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue
    );

    Screen build();
}
