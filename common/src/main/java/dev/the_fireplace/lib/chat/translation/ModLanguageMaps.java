package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.lib.chat.translation.proxy.LocaleProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModLanguageMaps
{
    private static final Map<String, LanguageMap> PRIMARY_LANGUAGE_MAPS = new ConcurrentHashMap<>();
    private static final Map<String, LanguageMap> FALLBACK_LANGUAGE_MAPS = new ConcurrentHashMap<>();

    static LanguageMap getPrimaryMap(String modId) {
        return PRIMARY_LANGUAGE_MAPS.computeIfAbsent(modId, unused -> new LanguageMap(modId, LocaleProxy.getInstance().getLocale()));
    }

    static LanguageMap getFallbackMap(String modId) {
        return FALLBACK_LANGUAGE_MAPS.computeIfAbsent(modId, unused -> new LanguageMap(modId, "en_us"));
    }

    public static void reloadLanguage() {
        PRIMARY_LANGUAGE_MAPS.clear();
    }
}
