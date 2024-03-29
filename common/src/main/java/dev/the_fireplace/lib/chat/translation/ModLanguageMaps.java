package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModLanguageMaps
{
    private static final Map<String, LanguageMap> PRIMARY_LANGUAGE_MAPS = new ConcurrentHashMap<>();
    private static final Map<String, LanguageMap> FALLBACK_LANGUAGE_MAPS = new ConcurrentHashMap<>();

    static LanguageMap getPrimaryMap(String modid) {
        return PRIMARY_LANGUAGE_MAPS.computeIfAbsent(modid, unused -> new LanguageMap(modid, LocaleProxy.getInstance().getLocale()));
    }

    static LanguageMap getFallbackMap(String modid) {
        return FALLBACK_LANGUAGE_MAPS.computeIfAbsent(modid, unused -> new LanguageMap(modid, "en_us"));
    }

    public static void reloadLanguage() {
        PRIMARY_LANGUAGE_MAPS.clear();
    }
}
