package dev.the_fireplace.lib.impl.storage;

import dev.the_fireplace.lib.api.io.JsonStorageReadBufferFactory;
import dev.the_fireplace.lib.api.storage.Readable;
import dev.the_fireplace.lib.api.storage.access.StorageReader;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import java.nio.file.Path;

public final class JsonStorageReader implements StorageReader {
    @Deprecated
    public static final StorageReader INSTANCE = new JsonStorageReader();

    private final JsonStorageReadBufferFactory jsonStorageBufferFactory;

    private JsonStorageReader() {
        jsonStorageBufferFactory = JsonStorageReadBufferFactory.getInstance();
    }

    @Override
    public void readTo(Readable readable) {
        Path filePath = JsonStoragePath.resolveJsonFilePath(readable);

        StorageReadBuffer reader = jsonStorageBufferFactory.create(filePath.toFile());
        readable.readFrom(reader);
    }
}
