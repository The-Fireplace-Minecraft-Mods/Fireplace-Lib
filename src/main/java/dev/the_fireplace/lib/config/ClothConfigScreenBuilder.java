package dev.the_fireplace.lib.config;

import com.google.common.collect.Lists;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.ConfigScreenBuilder;
import dev.the_fireplace.lib.compat.modmenu.ModMenuCompat;
import dev.the_fireplace.lib.compat.modmenu.OldModMenuCompat;
import dev.the_fireplace.lib.domain.config.ClothConfigDependencyTracker;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class ClothConfigScreenBuilder implements ConfigScreenBuilder {
    private final Translator translator;
    private final ConfigBuilder configBuilder;
    private final ConfigEntryBuilder entryBuilder;
    private final ClothConfigDependencyTracker dependencyTracker;
    private ConfigCategory category;

    public ClothConfigScreenBuilder(
        Translator translator,
        String titleTranslationKey,
        String initialCategoryTranslationKey,
        Screen parent,
        Runnable save
    ) {
        this.translator = translator;
        this.configBuilder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(translator.getTranslatedText(titleTranslationKey));
        this.entryBuilder = configBuilder.entryBuilder();
        this.category = configBuilder.getOrCreateCategory(translator.getTranslatedText(initialCategoryTranslationKey));
        if (hasOldModMenu()) {
            FireplaceLib.getLogger().info("Mod Menu 2.0.2 or older detected, enabling compat for cloth config GUI: {}.", translator.getTranslatedString(titleTranslationKey));
            this.configBuilder.setSavingRunnable(() -> {
                save.run();
                runOldModMenuCompat();
            });
        } else {
            this.configBuilder.setSavingRunnable(save);
        }
        this.dependencyTracker = new ClothConfigDependencyHandler();
    }

    private boolean hasOldModMenu() {
        Optional<ModContainer> modmenu = FabricLoader.getInstance().getModContainer("modmenu");
        try {
            if (modmenu.isPresent()) {
                SemanticVersion modMenuVersion = SemanticVersion.parse(modmenu.get().getMetadata().getVersion().getFriendlyString());
                return modMenuVersion.compareTo(SemanticVersion.parse("2.0.2")) < 1;
            }
        } catch (VersionParsingException e) {
            FireplaceLib.getLogger().error("Unable to parse mod menu version", e);
        }

        return false;
    }

    private void runOldModMenuCompat() {
        ModMenuCompat compat = new OldModMenuCompat();
        compat.reloadClothConfigGUIs();
    }

    @Override
    public ConfigScreenBuilder startCategory(String translationKey) {
        return startCategory(translationKey, new Object[0]);
    }

    @Override
    public ConfigScreenBuilder startCategory(String translationKey, Object... translationParameters) {
        Text categoryName = translator.getTranslatedText(translationKey, translationParameters);
        this.category = configBuilder.getOrCreateCategory(categoryName);
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
        return addStringField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, s -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addStringField(String optionTranslationBase, String currentValue, String defaultValue, Consumer<String> saveFunction, byte descriptionRowCount, Function<String, Optional<Text>> errorSupplier) {
        StringFieldBuilder builder = entryBuilder.startStrField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addEnumDropdown(optionTranslationBase, currentValue, defaultValue, dropdownEntries, saveFunction, descriptionRowCount, e -> Optional.empty());
    }

    @Override
    public <T extends Enum<T>> ConfigScreenBuilder addEnumDropdown(String optionTranslationBase, T currentValue, T defaultValue, Iterable<T> dropdownEntries, Consumer<T> saveFunction, byte descriptionRowCount, Function<T, Optional<Text>> errorSupplier) {
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
            descriptionRowCount,
            stringValue -> errorSupplier.apply(Enum.valueOf(currentValue.getDeclaringClass(), stringValue))
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
        return addStringDropdown(optionTranslationBase, currentValue, defaultValue, dropdownEntries, saveFunction, suggestionMode, descriptionRowCount, s -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addStringDropdown(String optionTranslationBase, String currentValue, String defaultValue, Iterable<String> dropdownEntries, Consumer<String> saveFunction, boolean suggestionMode, byte descriptionRowCount, Function<String, Optional<Text>> errorSupplier) {
        DropdownMenuBuilder<String> builder = entryBuilder.startStringDropdownMenu(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setSelections(dropdownEntries)
            .setSuggestionMode(suggestionMode)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addStringListField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, s -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addStringListField(String optionTranslationBase, List<String> currentValue, List<String> defaultValue, Consumer<List<String>> saveFunction, byte descriptionRowCount, Function<List<String>, Optional<Text>> errorSupplier) {
        StringListBuilder builder = entryBuilder.startStrList(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addFloatField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, f -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addFloatField(String optionTranslationBase, float currentValue, float defaultValue, Consumer<Float> saveFunction, float min, float max, byte descriptionRowCount, Function<Float, Optional<Text>> errorSupplier) {
        FloatFieldBuilder builder = entryBuilder.startFloatField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addFloatSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, f -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addFloatSlider(String optionTranslationBase, float currentValue, float defaultValue, Consumer<Float> saveFunction, float min, float max, byte descriptionRowCount, Function<Float, Optional<Text>> errorSupplier) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(
            translator.getTranslatedText(optionTranslationBase),
            FloatingPointClothConverter.floatToGuiValue(currentValue),
            FloatingPointClothConverter.floatToGuiValue(min),
            FloatingPointClothConverter.floatToGuiValue(max)
        )
            .setDefaultValue(FloatingPointClothConverter.floatToGuiValue(defaultValue))
            .setTextGetter(value -> Text.of(String.format("%.3f", FloatingPointClothConverter.guiValueToFloat(value))))
            .setSaveConsumer(newValue -> saveFunction.accept(FloatingPointClothConverter.guiValueToFloat(newValue)))
            .setErrorSupplier(newValue -> errorSupplier.apply(FloatingPointClothConverter.guiValueToFloat(newValue)));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addFloatingPointSlider(category, optionTranslationBase, entry, (byte)-1);
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
        return addFloatListField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, fl -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addFloatListField(String optionTranslationBase, List<Float> currentValue, List<Float> defaultValue, Consumer<List<Float>> saveFunction, byte descriptionRowCount, Function<List<Float>, Optional<Text>> errorSupplier) {
        FloatListBuilder builder = entryBuilder.startFloatList(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addDoubleField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, d -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addDoubleField(String optionTranslationBase, double currentValue, double defaultValue, Consumer<Double> saveFunction, double min, double max, byte descriptionRowCount, Function<Double, Optional<Text>> errorSupplier) {
        DoubleFieldBuilder builder = entryBuilder.startDoubleField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addDoubleSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, precision, d -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addDoubleSlider(String optionTranslationBase, double currentValue, double defaultValue, Consumer<Double> saveFunction, double min, double max, byte descriptionRowCount, byte precision, Function<Double, Optional<Text>> errorSupplier) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(
            translator.getTranslatedText(optionTranslationBase),
            FloatingPointClothConverter.doubleToGuiValue(currentValue, precision),
            FloatingPointClothConverter.doubleToGuiValue(min, precision),
            FloatingPointClothConverter.doubleToGuiValue(max, precision)
        )
            .setDefaultValue(FloatingPointClothConverter.doubleToGuiValue(defaultValue, precision))
            .setTextGetter(value -> Text.of(String.format("%." + precision + "f", FloatingPointClothConverter.guiValueToDouble(value, precision))))
            .setSaveConsumer(newValue -> saveFunction.accept(FloatingPointClothConverter.guiValueToDouble(newValue, precision)))
            .setErrorSupplier(newValue -> errorSupplier.apply(FloatingPointClothConverter.guiValueToDouble(newValue, precision)));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addFloatingPointSlider(category, optionTranslationBase, entry, precision);
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
        return addDoublePercentSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, precision, d -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addDoublePercentSlider(String optionTranslationBase, double currentValue, double defaultValue, Consumer<Double> saveFunction, byte descriptionRowCount, byte precision, Function<Double, Optional<Text>> errorSupplier) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(
            translator.getTranslatedText(optionTranslationBase),
            FloatingPointClothConverter.doubleToGuiValue(currentValue, precision),
            0,
            FloatingPointClothConverter.doubleToGuiValue(100, precision)
        )
            .setDefaultValue(FloatingPointClothConverter.doubleToGuiValue(defaultValue, precision))
            .setTextGetter(value -> Text.of(String.format("%." + precision + "f", FloatingPointClothConverter.guiValueToDouble(value, precision)) + "%"))
            .setSaveConsumer(newValue -> saveFunction.accept(FloatingPointClothConverter.guiValueToDouble(newValue, precision)))
            .setErrorSupplier(newValue -> errorSupplier.apply(FloatingPointClothConverter.guiValueToDouble(newValue, precision)));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addFloatingPointSlider(category, optionTranslationBase, entry, precision);
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
        return addDoubleListField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, dl -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addDoubleListField(String optionTranslationBase, List<Double> currentValue, List<Double> defaultValue, Consumer<List<Double>> saveFunction, byte descriptionRowCount, Function<List<Double>, Optional<Text>> errorSupplier) {
        DoubleListBuilder builder = entryBuilder.startDoubleList(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addLongField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, l -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addLongField(String optionTranslationBase, long currentValue, long defaultValue, Consumer<Long> saveFunction, long min, long max, byte descriptionRowCount, Function<Long, Optional<Text>> errorSupplier) {
        LongFieldBuilder builder = entryBuilder.startLongField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addLongSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, l -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addLongSlider(String optionTranslationBase, long currentValue, long defaultValue, Consumer<Long> saveFunction, long min, long max, byte descriptionRowCount, Function<Long, Optional<Text>> errorSupplier) {
        LongSliderBuilder builder = entryBuilder.startLongSlider(translator.getTranslatedText(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addLongListField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, ll -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addLongListField(String optionTranslationBase, List<Long> currentValue, List<Long> defaultValue, Consumer<List<Long>> saveFunction, byte descriptionRowCount, Function<List<Long>, Optional<Text>> errorSupplier) {
        LongListBuilder builder = entryBuilder.startLongList(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addIntField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, i -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addIntField(String optionTranslationBase, int currentValue, int defaultValue, Consumer<Integer> saveFunction, int min, int max, byte descriptionRowCount, Function<Integer, Optional<Text>> errorSupplier) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addIntSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, i -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addIntSlider(String optionTranslationBase, int currentValue, int defaultValue, Consumer<Integer> saveFunction, int min, int max, byte descriptionRowCount, Function<Integer, Optional<Text>> errorSupplier) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedText(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addIntListField(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, i -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addIntListField(String optionTranslationBase, List<Integer> currentValue, List<Integer> defaultValue, Consumer<List<Integer>> saveFunction, byte descriptionRowCount, Function<List<Integer>, Optional<Text>> errorSupplier) {
        IntListBuilder builder = entryBuilder.startIntList(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addShortField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, s -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addShortField(String optionTranslationBase, short currentValue, short defaultValue, Consumer<Short> saveFunction, short min, short max, byte descriptionRowCount, Function<Short, Optional<Text>> errorSupplier) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.shortValue()))
            .setErrorSupplier(newValue -> errorSupplier.apply(newValue.shortValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addShortSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, s -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addShortSlider(String optionTranslationBase, short currentValue, short defaultValue, Consumer<Short> saveFunction, short min, short max, byte descriptionRowCount, Function<Short, Optional<Text>> errorSupplier) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedText(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.shortValue()))
            .setErrorSupplier(newValue -> errorSupplier.apply(newValue.shortValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addByteField(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, b -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addByteField(String optionTranslationBase, byte currentValue, byte defaultValue, Consumer<Byte> saveFunction, byte min, byte max, byte descriptionRowCount, Function<Byte, Optional<Text>> errorSupplier) {
        IntFieldBuilder builder = entryBuilder.startIntField(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setMin(min)
            .setMax(max)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.byteValue()))
            .setErrorSupplier(newValue -> errorSupplier.apply(newValue.byteValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addByteSlider(optionTranslationBase, currentValue, defaultValue, saveFunction, min, max, descriptionRowCount, b -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addByteSlider(String optionTranslationBase, byte currentValue, byte defaultValue, Consumer<Byte> saveFunction, byte min, byte max, byte descriptionRowCount, Function<Byte, Optional<Text>> errorSupplier) {
        IntSliderBuilder builder = entryBuilder.startIntSlider(translator.getTranslatedText(optionTranslationBase), currentValue, min, max)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(newValue -> saveFunction.accept(newValue.byteValue()))
            .setErrorSupplier(newValue -> errorSupplier.apply(newValue.byteValue()));
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        AbstractConfigListEntry<?> entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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
        return addBoolToggle(optionTranslationBase, currentValue, defaultValue, saveFunction, descriptionRowCount, b -> Optional.empty());
    }

    @Override
    public ConfigScreenBuilder addBoolToggle(String optionTranslationBase, boolean currentValue, boolean defaultValue, Consumer<Boolean> saveFunction, byte descriptionRowCount, Function<Boolean, Optional<Text>> errorSupplier) {
        BooleanToggleBuilder builder = entryBuilder.startBooleanToggle(translator.getTranslatedText(optionTranslationBase), currentValue)
            .setDefaultValue(defaultValue)
            .setSaveConsumer(saveFunction)
            .setErrorSupplier(errorSupplier);
        attachDescription(optionTranslationBase, descriptionRowCount, builder);
        BooleanListEntry entry = builder.build();
        this.dependencyTracker.addOption(category, optionTranslationBase, entry);
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

    @Override
    public ConfigScreenBuilder addOptionDependency(
        String parentTranslationBase,
        String childTranslationBase,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue
    ) {
        this.dependencyTracker.addDependency(category, parentTranslationBase, childTranslationBase, shouldShowChildBasedOnParentValue);

        return this;
    }

    @Override
    public Screen build() {
        return configBuilder.build();
    }
}
