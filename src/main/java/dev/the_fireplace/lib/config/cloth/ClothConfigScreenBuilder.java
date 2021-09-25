package dev.the_fireplace.lib.config.cloth;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.*;
import dev.the_fireplace.lib.config.cloth.custombutton.CustomButtonFieldBuilder;
import dev.the_fireplace.lib.config.cloth.custombutton.CustomButtonOption;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothGenericOption;
import dev.the_fireplace.lib.config.cloth.optionbuilder.SubCategoryTracker;
import dev.the_fireplace.lib.domain.config.OptionBuilderFactory;
import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public final class ClothConfigScreenBuilder implements ConfigScreenBuilder {
    private static final OptionTypeConverter<Short, Integer> SHORT_TYPE_CONVERTER = new OptionTypeConverter<Short, Integer>() {
        @Override
        public Integer convertToClothType(Short source) {
            return Integer.valueOf(source);
        }

        @Override
        public Short convertFromClothType(Integer clothValue) {
            return clothValue.shortValue();
        }
    };
    private static final OptionTypeConverter<Byte, Integer> BYTE_TYPE_CONVERTER = new OptionTypeConverter<Byte, Integer>() {
        @Override
        public Integer convertToClothType(Byte source) {
            return Integer.valueOf(source);
        }

        @Override
        public Byte convertFromClothType(Integer clothValue) {
            return clothValue.byteValue();
        }
    };
    private final Translator translator;
    private final ConfigBuilder configBuilder;
    private final ConfigEntryBuilder entryBuilder;
    private final ClothConfigDependencyHandler dependencyTracker;
    private final OptionBuilderFactory optionBuilderFactory;
    private final Multimap<ConfigCategory, OptionBuilder<?>> categoryEntries;
    private ConfigCategory category;
    private SubCategoryTracker subCategory = null;

    public ClothConfigScreenBuilder(
        OptionBuilderFactory optionBuilderFactory,
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    ) {
        this.optionBuilderFactory = optionBuilderFactory;
        this.translator = translator;
        this.configBuilder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(translator.getTranslatedString(titleTranslationKey));
        this.entryBuilder = configBuilder.entryBuilder();
        this.category = configBuilder.getOrCreateCategory(translator.getTranslatedString(initialCategoryTranslationKey));
        this.configBuilder.setSavingRunnable(save);
        this.dependencyTracker = new ClothConfigDependencyHandler();
        this.categoryEntries = ArrayListMultimap.create();
    }

    @Override
    public void startCategory(String translationKey, Object... translationParameters) {
        String categoryName = translator.getTranslatedString(translationKey, translationParameters);
        this.category = configBuilder.getOrCreateCategory(categoryName);
        if (this.subCategory != null) {
            FireplaceLib.getLogger().warn("Sub-Category {} not explicitly ended before starting a new category! Ending it...", subCategory.getBuilder().getFieldNameKey());
            this.endSubCategory();
        }
    }

    @Override
    public void startSubCategory(String translationKey, Object... translationParameters) {
        this.subCategory = new SubCategoryTracker(new SubCategoryBuilder(entryBuilder.getResetButtonKey(), translator.getTranslatedString(translationKey, translationParameters)));
        this.categoryEntries.put(this.category, this.subCategory);
    }

    @Override
    public void endSubCategory() {
        this.subCategory = null;
    }

    @Override
    public OptionBuilder<String> addStringField(String optionTranslationBase, String currentValue, String defaultValue, Consumer<String> saveFunction) {
        StringFieldBuilder builder = entryBuilder.startStrField(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<String> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public <T extends Enum<T>> OptionBuilder<T> addEnumDropdown(String optionTranslationBase, T currentValue, T defaultValue, T[] dropdownEntries, Consumer<T> saveFunction) {
        OptionTypeConverter<T, String> enumTypeConverter = new OptionTypeConverter<T, String>() {
            @Override
            public String convertToClothType(T source) {
                return source.name();
            }

            @Override
            public T convertFromClothType(String clothValue) {
                //noinspection unchecked
                return (T) Enum.valueOf(currentValue.getClass(), clothValue);
            }
        };
        StringFieldBuilder builder = entryBuilder.startStrField(translator.getTranslatedString(optionTranslationBase), enumTypeConverter.convertToClothType(currentValue));

        OptionBuilder<T> optionBuilder = optionBuilderFactory.createDropdown(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            Sets.newHashSet(dropdownEntries),
            saveFunction,
            enumTypeConverter
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public DropdownOptionBuilder<String> addStringDropdown(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Iterable<String> dropdownEntries,
        Consumer<String> saveFunction
    ) {
        DropdownMenuBuilder<String> builder = entryBuilder.startStringDropdownMenu(translator.getTranslatedString(optionTranslationBase), currentValue);
        DropdownOptionBuilder<String> optionBuilder = optionBuilderFactory.createDropdown(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            dropdownEntries,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<String>> addStringListField(
        String optionTranslationBase,
        List<String> currentValue,
        List<String> defaultValue,
        Consumer<List<String>> saveFunction
    ) {
        StringListBuilder builder = entryBuilder.startStrList(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<List<String>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Float> addFloatField(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction
    ) {
        FloatFieldBuilder builder = entryBuilder.startFloatField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Float> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public DecimalSliderOptionBuilder<Float> addFloatSlider(
        String optionTranslationBase,
        float currentValue,
        float defaultValue,
        Consumer<Float> saveFunction,
        float min,
        float max
    ) {
        OptionTypeConverter<Float, Long> typeConverter = new FloatingPointClothConverter<>();
        LongSliderBuilder builder = entryBuilder.startLongSlider(
            translator.getTranslatedString(optionTranslationBase),
            typeConverter.convertToClothType(currentValue),
            typeConverter.convertToClothType(min),
            typeConverter.convertToClothType(max)
        );

        DecimalSliderOptionBuilder<Float> optionBuilder = optionBuilderFactory.createDecimalSlider(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            currentValue,
            min,
            max,
            typeConverter
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Float>> addFloatListField(
        String optionTranslationBase,
        List<Float> currentValue,
        List<Float> defaultValue,
        Consumer<List<Float>> saveFunction
    ) {
        FloatListBuilder builder = entryBuilder.startFloatList(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<List<Float>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Double> addDoubleField(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction
    ) {
        DoubleFieldBuilder builder = entryBuilder.startDoubleField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Double> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public DecimalSliderOptionBuilder<Double> addDoubleSlider(
        String optionTranslationBase,
        double currentValue,
        double defaultValue,
        Consumer<Double> saveFunction,
        double min,
        double max
    ) {
        OptionTypeConverter<Double, Long> typeConverter = new FloatingPointClothConverter<>();
        LongSliderBuilder builder = entryBuilder.startLongSlider(
            translator.getTranslatedString(optionTranslationBase),
            typeConverter.convertToClothType(currentValue),
            typeConverter.convertToClothType(min),
            typeConverter.convertToClothType(max)
        );

        DecimalSliderOptionBuilder<Double> optionBuilder = optionBuilderFactory.createDecimalSlider(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            currentValue,
            min,
            max,
            typeConverter
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Double>> addDoubleListField(
        String optionTranslationBase,
        List<Double> currentValue,
        List<Double> defaultValue,
        Consumer<List<Double>> saveFunction
    ) {
        DoubleListBuilder builder = entryBuilder.startDoubleList(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<List<Double>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Long> addLongField(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction
    ) {
        LongFieldBuilder builder = entryBuilder.startLongField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Long> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<Long> addLongSlider(
        String optionTranslationBase,
        long currentValue,
        long defaultValue,
        Consumer<Long> saveFunction,
        long min,
        long max
    ) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max);
        OptionBuilder<Long> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Long>> addLongListField(
        String optionTranslationBase,
        List<Long> currentValue,
        List<Long> defaultValue,
        Consumer<List<Long>> saveFunction
    ) {
        LongListBuilder builder = entryBuilder.startLongList(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<List<Long>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Integer> addIntField(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Integer> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<Integer> addIntSlider(
        String optionTranslationBase,
        int currentValue,
        int defaultValue,
        Consumer<Integer> saveFunction,
        int min,
        int max
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max);
        OptionBuilder<Integer> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Integer>> addIntListField(
        String optionTranslationBase,
        List<Integer> currentValue,
        List<Integer> defaultValue,
        Consumer<List<Integer>> saveFunction
    ) {
        IntListBuilder builder = entryBuilder.startIntList(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<List<Integer>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Short> addShortField(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Short> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            SHORT_TYPE_CONVERTER
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<Short> addShortSlider(
        String optionTranslationBase,
        short currentValue,
        short defaultValue,
        Consumer<Short> saveFunction,
        short min,
        short max
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max);
        OptionBuilder<Short> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            SHORT_TYPE_CONVERTER
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Short>> addShortListField(
        String optionTranslationBase,
        List<Short> currentValue,
        List<Short> defaultValue,
        Consumer<List<Short>> saveFunction
    ) {
        OptionTypeConverter<List<Short>, List<Integer>> typeConverter = new OptionTypeConverter<List<Short>, List<Integer>>() {
            @Override
            public List<Integer> convertToClothType(List<Short> source) {
                return source.stream().map(Integer::valueOf).collect(Collectors.toList());
            }

            @Override
            public List<Short> convertFromClothType(List<Integer> clothValue) {
                return clothValue.stream().map(Integer::shortValue).collect(Collectors.toList());
            }
        };
        IntListBuilder builder = entryBuilder.startIntList(translator.getTranslatedString(optionTranslationBase), typeConverter.convertToClothType(currentValue));
        OptionBuilder<List<Short>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            typeConverter
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public NumericOptionBuilder<Byte> addByteField(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction
    ) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedString(optionTranslationBase), currentValue);
        NumericOptionBuilder<Byte> optionBuilder = optionBuilderFactory.createNumeric(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            BYTE_TYPE_CONVERTER
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<Byte> addByteSlider(
        String optionTranslationBase,
        byte currentValue,
        byte defaultValue,
        Consumer<Byte> saveFunction,
        byte min,
        byte max
    ) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedString(optionTranslationBase), currentValue, min, max);
        OptionBuilder<Byte> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            BYTE_TYPE_CONVERTER
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<List<Byte>> addByteListField(
        String optionTranslationBase,
        List<Byte> currentValue,
        List<Byte> defaultValue,
        Consumer<List<Byte>> saveFunction
    ) {
        OptionTypeConverter<List<Byte>, List<Integer>> typeConverter = new OptionTypeConverter<List<Byte>, List<Integer>>() {
            @Override
            public List<Integer> convertToClothType(List<Byte> source) {
                return source.stream().map(Integer::valueOf).collect(Collectors.toList());
            }

            @Override
            public List<Byte> convertFromClothType(List<Integer> clothValue) {
                return clothValue.stream().map(Integer::byteValue).collect(Collectors.toList());
            }
        };
        IntListBuilder builder = entryBuilder.startIntList(translator.getTranslatedString(optionTranslationBase), typeConverter.convertToClothType(currentValue));
        OptionBuilder<List<Byte>> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction,
            typeConverter
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public OptionBuilder<Boolean> addBoolToggle(
        String optionTranslationBase,
        boolean currentValue,
        boolean defaultValue,
        Consumer<Boolean> saveFunction
    ) {
        BooleanToggleBuilder builder = entryBuilder.startBooleanToggle(translator.getTranslatedString(optionTranslationBase), currentValue);
        OptionBuilder<Boolean> optionBuilder = optionBuilderFactory.create(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    @Override
    public <T extends Screen & CustomButtonScreen<String>> CustomButtonBuilder<String> addCustomOptionButton(
        String optionTranslationBase,
        String currentValue,
        String defaultValue,
        Consumer<String> saveFunction,
        CustomButtonScreenFactory<String, T> buildOptionScreenFactory
    ) {
        CustomButtonFieldBuilder builder = new CustomButtonFieldBuilder(
            entryBuilder.getResetButtonKey(),
            translator.getTranslatedString(optionTranslationBase),
            currentValue,
            buildOptionScreenFactory
        );
        CustomButtonBuilder<String> optionBuilder = new CustomButtonOption(
            translator,
            builder,
            optionTranslationBase,
            defaultValue,
            saveFunction
        );
        registerBuilder(optionBuilder);

        return optionBuilder;
    }

    private void registerBuilder(OptionBuilder<?> optionBuilder) {
        categoryEntries.put(category, optionBuilder);
        if (subCategory != null) {
            subCategory.addEntry(optionBuilder);
        }
    }

    @Override
    public Screen build() {
        Map<OptionBuilder<?>, AbstractConfigListEntry<?>> builtOptions = new HashMap<>();

        for (Map.Entry<ConfigCategory, Collection<OptionBuilder<?>>> categoryEntries : categoryEntries.asMap().entrySet()) {
            builtOptions.putAll(buildCategory(categoryEntries.getKey(), categoryEntries.getValue()));
        }

        for (Map.Entry<ConfigCategory, Collection<OptionBuilder<?>>> categoryEntries : categoryEntries.asMap().entrySet()) {
            buildDependencies(categoryEntries.getValue(), builtOptions);
        }

        return configBuilder.build();
    }

    @SuppressWarnings({"unchecked", "rawtypes", "SuspiciousMethodCalls"})
    private void buildDependencies(Collection<OptionBuilder<?>> optionBuilders, Map<OptionBuilder<?>, AbstractConfigListEntry<?>> builtOptions) {
        for (OptionBuilder<?> optionBuilder : optionBuilders) {
            if (optionBuilder instanceof ClothGenericOption) {
                ClothGenericOption clothGenericOption = (ClothGenericOption) optionBuilder;
                clothGenericOption.getDependencies().forEach((key, value) ->
                    this.dependencyTracker.addDependency(builtOptions.get(key), builtOptions.get(optionBuilder), (Predicate) value)
                );
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<OptionBuilder<?>, AbstractConfigListEntry<?>> buildCategory(ConfigCategory configCategory, Collection<OptionBuilder<?>> optionBuilders) {
        Map<OptionBuilder<?>, AbstractConfigListEntry<?>> builtOptions = new HashMap<>();
        SubCategoryTracker subCategoryTracker = null;

        for (OptionBuilder optionBuilder : optionBuilders) {
            if (optionBuilder instanceof ClothGenericOption) {
                ClothGenericOption clothGenericOption = (ClothGenericOption) optionBuilder;
                AbstractConfigListEntry configListEntry = clothGenericOption.getFieldBuilder().build();

                this.dependencyTracker.addOption(configListEntry, clothGenericOption.getTypeConverter());
                builtOptions.put(optionBuilder, configListEntry);

                if (subCategoryTracker != null) {
                    subCategoryTracker.getBuilder().add(configListEntry);
                    if (subCategoryTracker.getLastEntry() == optionBuilder) {
                        finishSubCategory(configCategory, subCategoryTracker);
                        subCategoryTracker = null;
                    }
                } else {
                    configCategory.addEntry(configListEntry);
                }
            } else if (optionBuilder instanceof SubCategoryTracker) {
                subCategoryTracker = (SubCategoryTracker) optionBuilder;
                if (!subCategoryTracker.hasEntries()) {
                    finishSubCategory(configCategory, subCategoryTracker);
                    subCategoryTracker = null;
                }
            }
        }

        return builtOptions;
    }

    private void finishSubCategory(ConfigCategory configCategory, SubCategoryTracker tracker) {
        configCategory.addEntry(tracker.getBuilder().build());
    }
}
