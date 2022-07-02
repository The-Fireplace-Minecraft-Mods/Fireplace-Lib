package dev.the_fireplace.lib.chat.translation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.inject.Injector;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.io.injectables.DirectoryResolver;
import dev.the_fireplace.lib.domain.io.LoaderSpecificDirectories;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

final class LanguageMap
{
    /**
     * Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d", "%.2f"
     */
    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private final Map<String, String> languageList = new ConcurrentHashMap<>();
    private final LoaderSpecificDirectories loaderSpecificDirectories;
    private final String modId;

    LanguageMap(String modid, String locale) {
        Injector injector = FireplaceLibConstants.getInjector();
        DirectoryResolver directoryResolver = injector.getInstance(DirectoryResolver.class);
        this.loaderSpecificDirectories = injector.getInstance(LoaderSpecificDirectories.class);
        this.modId = modid;
        String langDir = directoryResolver.getLangDirectory(modid);
        try {
            JsonElement jsonelement = getLangJsonElement(locale, langDir);
            JsonObject jsonobject = GsonHelper.convertToJsonObject(jsonelement, "strings");

            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                String s = NUMERIC_VARIABLE_PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                this.languageList.put(entry.getKey(), s);
            }
        } catch (JsonParseException e) {
            FireplaceLibConstants.getLogger().error(langDir + locale + ".json is improperly formatted.", e);
        } catch (IOException ignored) {
        }
    }

    private JsonElement getLangJsonElement(String locale, String langDir) throws IOException {
        String langJsonPath = langDir + locale + ".json";
        InputStream inputstream = getInputStream(langJsonPath);
        if (inputstream == null) {
            FireplaceLibConstants.getLogger().error("Invalid locale: {}, defaulting to en_us.", locale);
            String defaultLangDir = langDir + "en_us.json";
            inputstream = getInputStream(defaultLangDir);
        }
        if (inputstream == null) {
            FireplaceLibConstants.getLogger().error("Unable to read language file in directory {}!", langDir);
            return new JsonObject();
        }
        JsonElement jsonelement = new Gson().fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
        inputstream.close();
        return jsonelement;
    }

    @Nullable
    private InputStream getInputStream(String langFileDirectory) {
        InputStream inputstream = LanguageMap.class.getResourceAsStream(langFileDirectory);
        if (inputstream == null) {
            Optional<Path> resource = loaderSpecificDirectories.getResource(modId, langFileDirectory);
            if (resource.isPresent()) {
                try {
                    inputstream = Files.newInputStream(resource.get());
                } catch (IOException exception) {
                    FireplaceLibConstants.getLogger().warn(String.format("Could not create input stream for resource %s", resource.get()), exception);
                }
            }
        }

        return inputstream;
    }

    String translateKeyFormat(String key, Object... format) {
        String translation = this.tryTranslateKey(key);

        try {
            return String.format(translation, format);
        } catch (IllegalFormatException e) {
            return "Format error: " + translation;
        }
    }

    private String tryTranslateKey(String key) {
        String translation = this.languageList.get(key);
        return translation == null ? key : translation;
    }

    boolean isKeyTranslated(String key) {
        return this.languageList.containsKey(key);
    }
}
