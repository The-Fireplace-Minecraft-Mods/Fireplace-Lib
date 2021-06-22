package dev.the_fireplace.lib.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.JsonFileReader;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.io.*;

@Implementation
@Singleton
public final class FileToJsonObject implements JsonFileReader {

    @Override
    @Nullable
    public JsonObject readJsonFile(File file) {
        JsonParser jsonParser = new JsonParser();
        try (BufferedReader br = new BufferedReader(new FileReader(file), Short.MAX_VALUE)) {
            JsonElement jsonElement = jsonParser.parse(br);
            if (jsonElement instanceof JsonObject jsonObject) {
                return jsonObject;
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
