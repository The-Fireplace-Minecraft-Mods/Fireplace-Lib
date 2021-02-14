package dev.the_fireplace.lib.api.io;

import com.google.gson.JsonObject;
import dev.the_fireplace.lib.api.storage.access.intermediary.StorageReadBuffer;
import dev.the_fireplace.lib.impl.io.JsonStorageReadBufferFactoryImpl;

import java.io.File;

public interface JsonStorageReadBufferFactory {
    static JsonStorageReadBufferFactory getInstance() {
        //noinspection deprecation
        return JsonStorageReadBufferFactoryImpl.INSTANCE;
    }

    StorageReadBuffer create(JsonObject obj);
    StorageReadBuffer create(File file);
}
