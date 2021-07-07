package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.domain.config.ClothConfigDependencyTracker;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigEntryAccessor;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ClothConfigDependencyHandler implements ClothConfigDependencyTracker {
    @ApiStatus.Internal
    public static final Set<AbstractConfigEntry<?>> DISABLED_ENTRIES = new HashSet<>();
    private static final Function<ConfigCategory, Map<String, AbstractConfigListEntry<?>>> CREATE_MAP = (unused) -> new HashMap<>();
    private final Map<ConfigCategory, Map<String, AbstractConfigListEntry<?>>> configEntries = new HashMap<>();
    private final Map<AbstractConfigListEntry<?>, Byte> entryPrecisions = new HashMap<>();

    @Override
    public void addOption(ConfigCategory category, String optionTranslationKey, AbstractConfigListEntry<?> option) {
        configEntries.computeIfAbsent(category, CREATE_MAP).put(optionTranslationKey, option);
    }

    @Override
    public void addFloatingPointSlider(ConfigCategory category, String optionTranslationKey, AbstractConfigListEntry<?> option, byte precision) {
        configEntries.computeIfAbsent(category, CREATE_MAP).put(optionTranslationKey, option);
        entryPrecisions.put(option, precision);
    }

    @Override
    public void addDependency(ConfigCategory category, String parentTranslationKey, String childTranslationKey, Function<Object, Boolean> shouldShowChildBasedOnParentValue) {
        addDependency(category, parentTranslationKey, category, childTranslationKey, shouldShowChildBasedOnParentValue);
    }

    @Override
    public void addDependency(
        ConfigCategory parentCategory,
        String parentTranslationKey,
        ConfigCategory childCategory,
        String childTranslationKey,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue
    ) {
        AbstractConfigListEntry<?> parentConfigEntry = configEntries.computeIfAbsent(parentCategory, CREATE_MAP).get(parentTranslationKey);
        AbstractConfigListEntry<?> childConfigEntry = configEntries.computeIfAbsent(childCategory, CREATE_MAP).get(childTranslationKey);
        if (childConfigEntry != null && parentConfigEntry != null) {
            createDependency(childConfigEntry, parentConfigEntry, shouldShowChildBasedOnParentValue);
        }
    }

    private void createDependency(AbstractConfigListEntry<?> childConfigEntry, AbstractConfigListEntry<?> parentConfigEntry, Function<Object, Boolean> shouldShowChildBasedOnParentValue) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        Supplier<Optional<String>> configEntryErrorSupplier = ((AbstractConfigEntryAccessor) parentConfigEntry).getErrorSupplier();
        Supplier<Optional<String>> dependencyCheckingErrorSupplier = addDependencyCheckToErrorSupplier(
            parentConfigEntry,
            childConfigEntry,
            shouldShowChildBasedOnParentValue,
            configEntryErrorSupplier
        );
        parentConfigEntry.setErrorSupplier(dependencyCheckingErrorSupplier);
    }

    @Nonnull
    private Supplier<Optional<String>> addDependencyCheckToErrorSupplier(
        AbstractConfigListEntry<?> parentConfigEntry,
        AbstractConfigListEntry<?> childConfigEntry,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue,
        @Nullable Supplier<Optional<String>> errorSupplier
    ) {
        return () -> {
            Object parentEntryGuiValue = unwrapGuiOptionValue(parentConfigEntry);

            if (!DISABLED_ENTRIES.contains(parentConfigEntry) && shouldShowChildBasedOnParentValue.apply(parentEntryGuiValue)) {
                showConfigEntry(childConfigEntry);
            } else {
                hideConfigEntry(childConfigEntry);
            }
            return errorSupplier != null ? errorSupplier.get() : Optional.empty();
        };
    }

    private Object unwrapGuiOptionValue(AbstractConfigListEntry<?> configEntry) {
        Object value = configEntry.getValue();

        if (!entryPrecisions.containsKey(configEntry)) {
            return value;
        }
        Byte precision = entryPrecisions.get(configEntry);
        if (precision < 0) {
            return FloatingPointClothConverter.guiValueToFloat((long)value);
        }

        return FloatingPointClothConverter.guiValueToDouble((long) value, precision);
    }

    private void hideConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.add(dependentEntry);
    }

    private void showConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.remove(dependentEntry);
    }
}
