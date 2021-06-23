package dev.the_fireplace.lib.chat.translation;

import dev.the_fireplace.lib.api.io.injectables.DirectoryResolver;
import dev.the_fireplace.lib.api.io.injectables.JarFileWalker;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Singleton
public final class I18n {
    
    private final DirectoryResolver directoryResolver;
    private final JarFileWalker jarFileWalker;

    @Inject
    public I18n(DirectoryResolver directoryResolver, JarFileWalker jarFileWalker) {
        this.directoryResolver = directoryResolver;
        this.jarFileWalker = jarFileWalker;
    }

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

    public boolean hasLocale(String modid, String locale) {
        InputStream inputstream = LanguageMap.class.getResourceAsStream(directoryResolver.getLangDirectory(modid) + locale + ".json");
        boolean exists = inputstream != null;
        if(exists) {
            try {
                inputstream.close();
            } catch (IOException ignored) {}
        }
        return exists;
    }

    public Set<String> getLocales(String modid) {
        try {
            Set<String> locales = new HashSet<>();
            jarFileWalker.getFiles(I18n.class.getResourceAsStream(directoryResolver.getLangDirectory(modid)).toString()).forEach(path -> locales.add(path.getFileName().toString().replace(".json", "")));
            return locales;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
