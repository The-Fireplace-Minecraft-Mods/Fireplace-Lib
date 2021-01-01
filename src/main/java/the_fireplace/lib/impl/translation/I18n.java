package the_fireplace.lib.impl.translation;

import the_fireplace.lib.api.io.Directories;
import the_fireplace.lib.api.io.JarFileWalker;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public final class I18n {

    public static String translateToLocalFormatted(String modid, String key, Object... format) {
        return hasPrimaryTranslation(modid, key) ? translateToPrimary(modid, key, format) : translateToFallback(modid, key, format);
    }

    private static String translateToPrimary(String modid, String key, Object... format) {
        return ModLanguageMaps.getPrimaryMap(modid).translateKeyFormat(key, format);
    }

    private static String translateToFallback(String modid, String key, Object... format) {
        return ModLanguageMaps.getFallbackMap(modid).translateKeyFormat(key, format);
    }

    private static boolean hasPrimaryTranslation(String modid, String key) {
        return ModLanguageMaps.getPrimaryMap(modid).isKeyTranslated(key);
    }

    public static boolean hasLocale(String modid, String locale) {
        InputStream inputstream = LanguageMap.class.getResourceAsStream(Directories.getLangDirectory(modid) + locale + ".json");
        boolean exists = inputstream != null;
        if(exists) {
            try {
                inputstream.close();
            } catch (IOException ignored) {}
        }
        return exists;
    }

    public static Set<String> getLocales(String modid) {
        try {
            Set<String> locales = new HashSet<>();
            JarFileWalker.getFiles(I18n.class.getResourceAsStream(Directories.getLangDirectory(modid)).toString()).forEach(path -> locales.add(path.getFileName().toString().replace(".json", "")));
            return locales;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
