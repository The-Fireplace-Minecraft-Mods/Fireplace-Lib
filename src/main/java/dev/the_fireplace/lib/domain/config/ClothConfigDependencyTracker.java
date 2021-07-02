package dev.the_fireplace.lib.domain.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;

import java.util.function.Function;

public interface ClothConfigDependencyTracker {
    void addOption(ConfigCategory category, String optionTranslationKey, AbstractConfigListEntry<?> option);
    void addDependency(
        ConfigCategory category,
        String parentTranslationKey,
        String childTranslationKey,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue
    );
    void addDependency(
        ConfigCategory parentCategory,
        String parentTranslationKey,
        ConfigCategory childCategory,
        String childTranslationKey,
        Function<Object, Boolean> shouldShowChildBasedOnParentValue
    );
}
