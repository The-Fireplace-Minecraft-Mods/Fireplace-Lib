package the_fireplace.lib.impl.translation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonHelper;
import the_fireplace.lib.api.io.Directories;
import the_fireplace.lib.impl.FireplaceLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

final class LanguageMap {
    /** Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d", "%.2f" */
    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private final Map<String, String> languageList = new ConcurrentHashMap<>();

    LanguageMap(String modid, String locale) {
        String langDir = Directories.getLangDirectory(modid);
        try {
            JsonElement jsonelement = getLangJsonElement(locale, langDir);
            JsonObject jsonobject = JsonHelper.asObject(jsonelement, "strings");

            for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                String s = NUMERIC_VARIABLE_PATTERN.matcher(JsonHelper.asString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                this.languageList.put(entry.getKey(), s);
            }
        } catch (JsonParseException e) {
            FireplaceLib.getLogger().error(langDir + locale + ".json is improperly formatted.", e);
        } catch(IOException ignored) {}
    }

    private JsonElement getLangJsonElement(String locale, String langDir) throws IOException {
        InputStream inputstream = LanguageMap.class.getResourceAsStream(langDir + locale + ".json");
        if (inputstream == null) {
            FireplaceLib.getLogger().error("Invalid locale: {}, defaulting to en_us.", locale);
            inputstream = LanguageMap.class.getResourceAsStream(langDir + "en_us.json");
        }
        JsonElement jsonelement = (new Gson()).fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
        inputstream.close();
        return jsonelement;
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
