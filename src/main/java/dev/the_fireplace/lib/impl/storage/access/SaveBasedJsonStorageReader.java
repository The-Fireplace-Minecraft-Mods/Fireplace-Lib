package dev.the_fireplace.lib.impl.storage.access;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.JsonFileReader;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import java.nio.file.Path;

public final class SaveBasedJsonStorageReader implements SaveBasedStorageReader {
    @Deprecated
    public static final SaveBasedStorageReader INSTANCE = new SaveBasedJsonStorageReader();
    private final JsonFileReader fileReader;

    private SaveBasedJsonStorageReader() {
        fileReader = JsonFileReader.getInstance();
    }

    @Override
    public void readTo(SaveBasedSerializable readable) {
        Path filePath = JsonStoragePath.resolveSaveBasedJsonFilePath(readable);

        JsonObject obj = fileReader.readJsonFile(filePath.toFile());
        if (obj == null) {
            obj = new JsonObject();
        }

        StorageReadBuffer reader = new JsonStorageReadBuffer(obj);
        readable.readFrom(reader);
    }
}
