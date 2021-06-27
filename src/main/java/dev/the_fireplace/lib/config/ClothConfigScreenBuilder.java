package dev.the_fireplace.lib.config;

import com.google.common.collect.Lists;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigEntryAccessor;
import io.netty.util.internal.ConcurrentSet;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "WeakerAccess", "unused", "unchecked"})
@Environment(EnvType.CLIENT)
public final class ClothConfigScreenBuilder implements ConfigScreenBuilder {
    private final Translator translator;
    private final ConfigEntryBuilder entryBuilder;
    private ConfigCategory category;

    private final Map<String, BooleanListEntry> booleanEntries = new HashMap<>();
    private final Map<String, AbstractConfigListEntry<?>> configEntries = new HashMap<>();

    protected ClothConfigScreenBuilder(Translator translator, ConfigEntryBuilder entryBuilder, ConfigCategory initialCategory) {
        this.translator = translator;
        this.entryBuilder = entryBuilder;
        this.category = initialCategory;
    }

    @Override
    public ConfigScreenBuilder startCategory(ConfigCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public ConfigScreenBuilder addStringField(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction
    ) {
        return addStringField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addStringField(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction,
        byte descriptionRowCount
    ) {
        StringFieldBuilder builder = entryBuilder.startStrField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(
        String optionTranslationBase,
        T currentValue,
        T defaultValue,
        Iterable<T> dropdownEntries,
        Consumer<T> saveFunction
    ) {
        return addEnumDropdown(
            optionTranslationBase,
            currentValue,
            defaultValue,
            dropdownEntries,
            saveFunction,
            (byte)1
        );
    }

    @Override
    public <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(
        String optionTranslationBase,
        T currentValue,
        T defaultValue,
        Iterable<T> dropdownEntries,
        Consumer<T> saveFunction,
        byte descriptionRowCount
    ) {
        List<String> stringEntries = new ArrayList<>();
        for (T entry: dropdownEntries) {
            stringEntries.add(entry.toString());
        }
        return addStringDropdown(
            optionTranslationBase,
            currentValue.toString(),
            defaultValue.toString(),
            stringEntries,
            stringValue -> saveFunction.accept(Enum.valueOf(currentValue.getDeclaringClass(), stringValue)),
            false,
            descriptionRowCount
        );
    }

    @Override
    public ConfigScreenBuilder addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction,
        boolean suggestionMode
    ) {
        return addStringDropdown(optionTranslationBase, currentValue, defaultValue, dropdownEntries, saveFunction, suggestionMode, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction,
        boolean suggestionMode,
        byte descriptionRowCount
    ) {
        DropdownMenuBuilder<String> builder = entryBuilder.startStringDropdownMenu(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setSelections(dropdownEntries);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction
    ) {
        return addStringListField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction,
        byte descriptionRowCount
    ) {
        StringListBuilder builder = entryBuilder.startStrList(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction
    ) {
        return addFloatField(optionTranslationBase, currentValue, defaultValue, saveFunction, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max
    ) {
        return addFloatField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount
    ) {
        FloatFieldBuilder builder = entryBuilder.startFloatField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max
    ) {
        return addFloatSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max,
        byte descriptionRowCount
    ) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedString(optionTranslationBase), (long) (currentValue * 1000), (long) (min * 1000), (long) (max * 1000))
            .setDefaultValue((long) (defaultValue * 1000))
            .setTextGetter(value -> String.format("%.3f", value / 1000f))
            .setSaveConsumer(newValue -> saveFunction.accept(newValue / 1000f));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction
    ) {
        return addFloatListField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction,
        byte descriptionRowCount
    ) {
        FloatListBuilder builder = entryBuilder.startFloatList(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction
    ) {
        return addDoubleField(optionTranslationBase, currentValue, defaultValue, saveFunction, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max
    ) {
        return addDoubleField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount
    ) {
        DoubleFieldBuilder builder = entryBuilder.startDoubleField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max
    ) {
        return addDoubleSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount
    ) {
        return addDoubleSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, (byte)3);
    }

    @Override
    public ConfigScreenBuilder addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max,
        byte descriptionRowCount,
        byte precision
    ) {
        long factor = (long) Math.pow(10, precision);
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedString(optionTranslationBase), (long) (currentValue * factor), (long) (min * factor), (long) (max * factor))
            .setDefaultValue((long) (defaultValue * factor))
            .setTextGetter(value -> String.format("%." + precision + "f", value / (double)factor))
            .setSaveConsumer(newValue -> saveFunction.accept(newValue / (double)factor));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addDoublePercentSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction
    ) {
        return addDoublePercentSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addDoublePercentSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        byte descriptionRowCount,
        byte precision
    ) {
        long factor = (long) Math.pow(10, precision);
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedString(optionTranslationBase), (long) (currentValue * factor), 0, 100 * factor)
            .setDefaultValue((long) (defaultValue * factor))
            .setTextGetter(value -> String.format("%." + precision + "f", value / (double)factor) + "%")
            .setSaveConsumer(newValue -> saveFunction.accept(newValue / (double)factor));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction
    ) {
        return addDoubleListField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction,
        byte descriptionRowCount
    ) {
        DoubleListBuilder builder = entryBuilder.startDoubleList(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction
    ) {
        return addLongField(optionTranslationBase, currentValue, defaultValue, saveFunction, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max
    ) {
        return addLongField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount
    ) {
        LongFieldBuilder builder = entryBuilder.startLongField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max
    ) {
        return addLongSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max,
        byte descriptionRowCount
    ) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction
    ) {
        return addLongListField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction,
        byte descriptionRowCount
    ) {
        LongListBuilder builder = entryBuilder.startLongList(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction
    ) {
        return addIntField(optionTranslationBase, currentValue, defaultValue, saveFunction, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max
    ) {
        return addIntField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max
    ) {
        return addIntSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max,
        byte descriptionRowCount
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction
    ) {
        return addIntListField(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction,
        byte descriptionRowCount
    ) {
        IntListBuilder builder = entryBuilder.startIntList(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction
    ) {
        return addShortField(optionTranslationBase, currentValue, defaultValue, saveFunction, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max
    ) {
        return addShortField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.shortValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max
    ) {
        return addShortSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max,
        byte descriptionRowCount
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.shortValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction
    ) {
        return addByteField(optionTranslationBase, currentValue, defaultValue, saveFunction, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    public ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max
    ) {
        return addByteField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.byteValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max
    ) {
        return addByteSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max,
        byte descriptionRowCount
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.byteValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    @Override
    public ConfigScreenBuilder addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction
    ) {
        return addBoolToggle(optionTranslationBase, currentValue, defaultValue, saveFunction, (byte)1);
    }

    @Override
    public ConfigScreenBuilder addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction,
        byte descriptionRowCount
    ) {
        BooleanToggleBuilder builder = entryBuilder.startBooleanToggle(translator.getTranslatedString(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        BooleanListEntry entry = builder.build();
        booleanEntries.put(optionTranslationBase, entry);
        configEntries.put(optionTranslationBase, entry);
        category.addEntry(entry);
        
        return this;
    }

    private void attachDescription(String optionTranslationBase, byte descriptionRowCount, FieldBuilder<?, ?> builder) {
        if (descriptionRowCount <= 0) {
            return;
        }
        try {
            Method setTooltip = builder.getClass().getMethod("setTooltip", Text[].class);
            if (descriptionRowCount == 1) {
                setTooltip.invoke(builder, (Object) new Text[]{translator.getTranslatedText(optionTranslationBase + ".desc")});
            } else {
                setTooltip.invoke(builder, (Object) genDescriptionTranslatables(optionTranslationBase + ".desc", descriptionRowCount));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set tooltip for field builder of type " + builder.getClass(), e);
        }
    }

    private Text[] genDescriptionTranslatables(String baseKey, int count) {
        List<Text> texts = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            texts.add(translator.getTranslatedText(baseKey + "[" + i + "]"));
        }
        return texts.toArray(new Text[0]);
    }

    public static final Set<AbstractConfigEntry<?>> DISABLED_ENTRIES = new ConcurrentSet<>();

    @Override
    public ConfigScreenBuilder addBooleanOptionDependency(String dependentTranslationBase, String dependsOnTranslationBase) {
        AbstractConfigListEntry<?> dependentEntry = configEntries.get(dependentTranslationBase);
        BooleanListEntry dependencyEntry = booleanEntries.get(dependsOnTranslationBase);
        if (dependentEntry != null && dependencyEntry != null) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            Supplier<Optional<Text>> previousErrorSupplier = ((AbstractConfigEntryAccessor)dependencyEntry).getErrorSupplier();
            dependencyEntry.setErrorSupplier(() -> {
                if (dependencyEntry.getValue()) {
                    showConfigEntry(dependentEntry);
                } else {
                    hideConfigEntry(dependentEntry);
                }
                return previousErrorSupplier != null ? previousErrorSupplier.get() : Optional.empty();
            });
        }

        return this;
    }

    private void hideConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.add(dependentEntry);
    }

    private void showConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.remove(dependentEntry);
    }
}
