package dev.the_fireplace.lib.api.io;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.impl.io.FileToJsonObject;

import javax.annotation.Nullable;
import java.io.File;

public interface JsonFileReader {
    static JsonFileReader getInstance() {
        //noinspection deprecation
        return FileToJsonObject.INSTANCE;
    }

    @Nullable
    JsonObject readJsonFile(File file);
}
