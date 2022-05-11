package dev.the_fireplace.lib.config.dummy;

import dev.the_fireplace.lib.api.client.interfaces.*;
import dev.the_fireplace.lib.config.dummy.optionbuilder.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.Consumer;

public final class DummyConfigScreenBuilder implements ConfigScreenBuilder
{
    @Override
    public void startCategory(String translationKey, Object... translationParameters) {

    }

    @Override
    public void startSubCategory(String translationKey, Object... translationParameters) {

    }

    @Override
    public void endSubCategory() {

    }

    @Override
    public OptionBuilder<String> addStringField(String optionTranslationBase, String currentValue, String defaultValue, Consumer<String> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public DropdownOptionBuilder<String> addStringDropdown(String optionTranslationBase, String currentValue, String defaultValue, Iterable<String> dropdownEntries, Consumer<String> saveFunction) {
        return new DummyDropdownOptionBuilder<>();
    }

    @Override
    public <T extends Enum<T>> OptionBuilder<T> addEnumDropdown(String optionTranslationBase, T currentValue, T defaultValue, T[] dropdownEntries, Consumer<T> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<String>> addStringListField(String optionTranslationBase, List<String> currentValue, List<String> defaultValue, Consumer<List<String>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Float> addFloatField(String optionTranslationBase, float currentValue, float defaultValue, Consumer<Float> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public DecimalSliderOptionBuilder<Float> addFloatSlider(String optionTranslationBase, float currentValue, float defaultValue, Consumer<Float> saveFunction, float min, float max) {
        return new DummyDecimalSliderOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Float>> addFloatListField(String optionTranslationBase, List<Float> currentValue, List<Float> defaultValue, Consumer<List<Float>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Double> addDoubleField(String optionTranslationBase, double currentValue, double defaultValue, Consumer<Double> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public DecimalSliderOptionBuilder<Double> addDoubleSlider(String optionTranslationBase, double currentValue, double defaultValue, Consumer<Double> saveFunction, double min, double max) {
        return new DummyDecimalSliderOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Double>> addDoubleListField(String optionTranslationBase, List<Double> currentValue, List<Double> defaultValue, Consumer<List<Double>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Long> addLongField(String optionTranslationBase, long currentValue, long defaultValue, Consumer<Long> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public OptionBuilder<Long> addLongSlider(String optionTranslationBase, long currentValue, long defaultValue, Consumer<Long> saveFunction, long min, long max) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Long>> addLongListField(String optionTranslationBase, List<Long> currentValue, List<Long> defaultValue, Consumer<List<Long>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Integer> addIntField(String optionTranslationBase, int currentValue, int defaultValue, Consumer<Integer> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public OptionBuilder<Integer> addIntSlider(String optionTranslationBase, int currentValue, int defaultValue, Consumer<Integer> saveFunction, int min, int max) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Integer>> addIntListField(String optionTranslationBase, List<Integer> currentValue, List<Integer> defaultValue, Consumer<List<Integer>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Short> addShortField(String optionTranslationBase, short currentValue, short defaultValue, Consumer<Short> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public OptionBuilder<Short> addShortSlider(String optionTranslationBase, short currentValue, short defaultValue, Consumer<Short> saveFunction, short min, short max) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Short>> addShortListField(String optionTranslationBase, List<Short> currentValue, List<Short> defaultValue, Consumer<List<Short>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public NumericOptionBuilder<Byte> addByteField(String optionTranslationBase, byte currentValue, byte defaultValue, Consumer<Byte> saveFunction) {
        return new DummyNumericOptionBuilder<>();
    }

    @Override
    public OptionBuilder<Byte> addByteSlider(String optionTranslationBase, byte currentValue, byte defaultValue, Consumer<Byte> saveFunction, byte min, byte max) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<List<Byte>> addByteListField(String optionTranslationBase, List<Byte> currentValue, List<Byte> defaultValue, Consumer<List<Byte>> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public OptionBuilder<Boolean> addBoolToggle(String optionTranslationBase, boolean currentValue, boolean defaultValue, Consumer<Boolean> saveFunction) {
        return new DummyOptionBuilder<>();
    }

    @Override
    public <T extends Screen & CustomButtonScreen<String>> CustomButtonBuilder<String> addCustomOptionButton(String optionTranslationBase, String currentValue, String defaultValue, Consumer<String> saveFunction, CustomButtonScreenFactory<String, T> buildOptionScreenFactory) {
        return new DummyCustomButtonBuilder<>();
    }

    @Override
    public Screen build() {
        return Minecraft.getInstance().screen;
    }
}
