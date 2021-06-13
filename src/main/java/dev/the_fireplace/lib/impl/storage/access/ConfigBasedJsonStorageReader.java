package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.io.JsonFileReader;
import dev.the_fireplace.lib.api.storage.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;

@Implementation
@Singleton
public final class ConfigBasedJsonStorageReader implements ConfigBasedStorageReader {
    private final JsonFileReader fileReader;

    @Inject
    public ConfigBasedJsonStorageReader(JsonFileReader jsonFileReader) {
        fileReader = jsonFileReader;
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
