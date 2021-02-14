package dev.the_fireplace.lib.impl.io;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.io.JsonFileReader;
import dev.the_fireplace.lib.api.io.JsonStorageReadBufferFactory;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;

import java.io.File;

public final class JsonStorageReadBufferFactoryImpl implements JsonStorageReadBufferFactory {
    @Deprecated
    public static final JsonStorageReadBufferFactory INSTANCE = new JsonStorageReadBufferFactoryImpl();

    private JsonStorageReadBufferFactoryImpl(){}

    @Override
    public StorageReadBuffer create(JsonObject obj) {
        return new JsonStorageReadBuffer(obj);
    }

    @Override
    public StorageReadBuffer create(File file) {
        JsonObject obj = JsonFileReader.getInstance().readJsonFile(file);
        if (obj == null) {
            return create(new JsonObject());
        }
        return create(obj);
    }
}
