package dev.the_fireplace.lib.impl.storage;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.JsonFileReader;
import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import java.nio.file.Path;

public final class ConfigBasedJsonStorageReader implements ConfigBasedStorageReader {
    @Deprecated
    public static final ConfigBasedStorageReader INSTANCE = new ConfigBasedJsonStorageReader();
    private final JsonFileReader fileReader;

    private ConfigBasedJsonStorageReader() {
        fileReader = JsonFileReader.getInstance();
    }

    @Override
    public void readTo(ConfigBasedSerializable readable) {
        Path filePath = JsonStoragePath.resolveConfigBasedJsonFilePath(readable);

        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }
}
