package dev.the_fireplace.lib.io.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.ConfigBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.JsonFileReader;
import dev.the_fireplace.lib.api.io.interfaces.ConfigBasedSerializable;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;

@Implementation
@Singleton
public final class ConfigBasedJsonStorageReader implements ConfigBasedStorageReader {
    private final JsonFileReader fileReader;
    private final JsonStoragePath jsonStoragePath;

    @Inject
    public ConfigBasedJsonStorageReader(JsonFileReader jsonFileReader, JsonStoragePath jsonStoragePath) {
        this.fileReader = jsonFileReader;
        this.jsonStoragePath = jsonStoragePath;
    }

    @Override
    public void readTo(ConfigBasedSerializable readable) {
        Path filePath = jsonStoragePath.resolveConfigBasedJsonFilePath(readable);

        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }
}
