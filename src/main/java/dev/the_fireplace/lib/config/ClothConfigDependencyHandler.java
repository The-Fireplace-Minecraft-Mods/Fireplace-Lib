package dev.the_fireplace.lib.config;

import dev.the_fireplace.lib.domain.config.ClothConfigDependencyTracker;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigEntryAccessor;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;
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

    @Override
    public void addOption(ConfigCategory category, String optionTranslationKey, AbstractConfigListEntry<?> option) {
        configEntries.computeIfAbsent(category, CREATE_MAP).put(optionTranslationKey, option);
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
        Supplier<Optional<Text>> configEntryErrorSupplier = ((AbstractConfigEntryAccessor) parentConfigEntry).getErrorSupplier();
        Supplier<Optional<Text>> dependencyCheckingErrorSupplier = addDependencyCheckToErrorSupplier(
            parentConfigEntry,
            childConfigEntry,
            shouldShowChildBasedOnParentValue,
            configEntryErrorSupplier
        );
        parentConfigEntry.setErrorSupplier(dependencyCheckingErrorSupplier);
    }

    @Nonnull
    private Supplier<Optional<Text>> addDependencyCheckToErrorSupplier(
        AbstractConfigListEntry<?> parentConfigEntry,
        AbstractConfigListEntry<?> childConfigEntry,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue,
        @Nullable Supplier<Optional<Text>> errorSupplier
    ) {
        return () -> {
            if (shouldShowChildBasedOnParentValue.apply(parentConfigEntry.getValue())) {
                showConfigEntry(childConfigEntry);
            } else {
                hideConfigEntry(childConfigEntry);
            }
            return errorSupplier != null ? errorSupplier.get() : Optional.empty();
        };
    }

    private void hideConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.add(dependentEntry);
    }

    private void showConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.remove(dependentEntry);
    }
}
