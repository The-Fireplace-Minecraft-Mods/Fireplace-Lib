package dev.the_fireplace.lib.chat.translation;

import javax.inject.Singleton;

@Singleton
public final class I18n {
    public String translateToLocalFormatted(String modid, String key, Object... format) {
        return hasPrimaryTranslation(modid, key) ? translateToPrimary(modid, key, format) : translateToFallback(modid, key, format);
    }

    private String translateToPrimary(String modid, String key, Object... format) {
        return ModLanguageMaps.getPrimaryMap(modid).translateKeyFormat(key, format);
    }

    private String translateToFallback(String modid, String key, Object... format) {
        return ModLanguageMaps.getFallbackMap(modid).translateKeyFormat(key, format);
    }

    private boolean hasPrimaryTranslation(String modid, String key) {
        return ModLanguageMaps.getPrimaryMap(modid).isKeyTranslated(key);
    }
}
