package dev.the_fireplace.lib.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.io.injectables.JsonFileReader;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.io.*;

@Implementation
@Singleton
public final class ExternalResourceToJson implements JsonFileReader
{

    @Override
    @Nullable
    public JsonObject readJsonFile(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            return readJsonFromReader(fileReader);
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Nullable
    public JsonObject readJsonFromStream(InputStream stream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(stream)) {
            return readJsonFromReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject readJsonFromReader(Reader reader) throws IOException {
        JsonParser jsonParser = new JsonParser();
        try (BufferedReader br = new BufferedReader(reader, Short.MAX_VALUE)) {
            JsonElement jsonElement = jsonParser.parse(br);
            if (jsonElement instanceof JsonObject) {
                return (JsonObject) jsonElement;
            }
        } catch (JsonParseException e) {
            FireplaceLibConstants.getLogger().warn("Unable to parse json file!", e);
        }
        return null;
    }
}
