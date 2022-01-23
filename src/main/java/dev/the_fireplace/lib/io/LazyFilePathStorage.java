package dev.the_fireplace.lib.io;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.FilePathStorage;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;
import dev.the_fireplace.lib.api.lazyio.interfaces.Defaultable;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Implementation("dev.the_fireplace.lib.api.io.injectables.FilePathStorage")
@Singleton
public final class LazyFilePathStorage implements Config, Defaultable, FilePathStorage
{
    private final ConcurrentMap<String, String> lazyMemory;
    private final ConfigStateManager configStateManager;

    @Inject
    public LazyFilePathStorage(ConfigStateManager configStateManager) {
        this.configStateManager = configStateManager;
        this.lazyMemory = new ConcurrentHashMap<>(1);
        configStateManager.initialize(this);
    }

    @Override
    public String getSubfolderName() {
        return FireplaceLib.MODID;
    }

    @Override
    public String getId() {
        return "paths";
    }

    @Override
    public void readFrom(StorageReadBuffer buffer) {
        for (String key : buffer.getKeys()) {
            lazyMemory.put(key, buffer.readString(key, ""));
        }
    }

    @Override
    public void writeTo(StorageWriteBuffer buffer) {
        for (Map.Entry<String, String> entry : lazyMemory.entrySet()) {
            buffer.writeString(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean isDefault() {
        return lazyMemory.isEmpty() || lazyMemory.values().stream().allMatch(String::isEmpty);
    }

    @Override
    @Nullable
    public String getFilePath(String key) {
        return lazyMemory.get(key);
    }

    @Override
    public void storeFilePath(String key, String path) {
        lazyMemory.put(key, path);
        configStateManager.save(this);
    }
}
