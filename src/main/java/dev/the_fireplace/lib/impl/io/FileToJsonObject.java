package dev.the_fireplace.lib.impl.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.the_fireplace.lib.api.io.JsonFileReader;

import javax.annotation.Nullable;
import java.io.*;

public final class FileToJsonObject implements JsonFileReader {
    @Deprecated
    public static final JsonFileReader INSTANCE = new FileToJsonObject();

    private FileToJsonObject(){}

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
