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

    LanguageMap(String modId, String locale) {
        Injector injector = FireplaceLibConstants.getInjector();
        DirectoryResolver directoryResolver = injector.getInstance(DirectoryResolver.class);
        this.loaderSpecificDirectories = injector.getInstance(LoaderSpecificDirectories.class);
        this.modId = modId;
        String localizationDirectory = directoryResolver.getLangDirectory(modId);
        try {
            JsonObject jsonObject = getTranslationsAsJson(locale, localizationDirectory);

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String translatedString = GsonHelper.convertToString(entry.getValue(), entry.getKey());
                translatedString = standardizeNumberPlaceholders(translatedString);
                this.languageList.put(entry.getKey(), translatedString);
            }
        } catch (JsonParseException e) {
            FireplaceLibConstants.getLogger().error(localizationDirectory + locale + ".json is improperly formatted.", e);
        } catch (IOException ignored) {
        }
    }

    private static String standardizeNumberPlaceholders(String translatedString) {
        return NUMERIC_VARIABLE_PATTERN
            .matcher(translatedString)
            .replaceAll("%$1s");
    }

    private JsonObject getTranslationsAsJson(String locale, String localizationDirectory) throws IOException {
        String localeJsonPath = localizationDirectory + locale + ".json";
        InputStream inputStream = getInputStream(localeJsonPath);
        if (inputStream == null) {
            FireplaceLibConstants.getLogger().error("Invalid locale: {}, defaulting to en_us.", locale);
            String defaultLocaleJsonPath = localizationDirectory + "en_us.json";
            inputStream = getInputStream(defaultLocaleJsonPath);
        }
        if (inputStream == null) {
            FireplaceLibConstants.getLogger().error("Unable to read language file in directory {}!", localizationDirectory);
            return new JsonObject();
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
        inputStream.close();
        return jsonObject;
    }

    @Nullable
    private InputStream getInputStream(String localeJsonPath) {
        InputStream inputStream = LanguageMap.class.getResourceAsStream(localeJsonPath);
        if (inputStream != null) {
            return inputStream;
        }
        Optional<Path> resource = loaderSpecificDirectories.getResource(modId, localeJsonPath);
        if (resource.isPresent()) {
            try {
                inputStream = Files.newInputStream(resource.get());
            } catch (IOException exception) {
                String logMessage = String.format("Could not create input stream for resource %s", resource.get());
                FireplaceLibConstants.getLogger().warn(logMessage, exception);
            }
        }

        return inputStream;
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
