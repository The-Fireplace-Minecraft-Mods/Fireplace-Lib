package dev.the_fireplace.lib.api.io.injectables;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import java.io.File;

public interface JsonFileReader {
    @Nullable
    JsonObject readJsonFile(File file);
}
