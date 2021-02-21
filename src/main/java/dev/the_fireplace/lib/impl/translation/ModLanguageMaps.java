package dev.the_fireplace.lib.impl.translation;

import dev.the_fireplace.lib.impl.config.FLConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class ModLanguageMaps {
    private static final Map<String, LanguageMap> PRIMARY_LANGUAGE_MAPS = new ConcurrentHashMap<>();
    private static final Map<String, LanguageMap> FALLBACK_LANGUAGE_MAPS = new ConcurrentHashMap<>();

    static LanguageMap getPrimaryMap(String modid) {
        return PRIMARY_LANGUAGE_MAPS.computeIfAbsent(modid, unused -> new LanguageMap(modid, FLConfig.getData().getLocale()));
    }

    static LanguageMap getFallbackMap(String modid) {
        return FALLBACK_LANGUAGE_MAPS.computeIfAbsent(modid, unused -> new LanguageMap(modid, "en_us"));
    }
}
