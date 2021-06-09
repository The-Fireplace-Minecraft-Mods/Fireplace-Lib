package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

@ThreadSafe
public abstract class ThreadsafeLazySavable implements SaveBasedSerializable {
    private final SaveBasedStorageReader saveBasedStorageReader = SaveBasedStorageReader.getInstance();
    private final SaveBasedStorageWriter saveBasedStorageWriter = SaveBasedStorageWriter.getInstance();
    @SuppressWarnings("WeakerAccess")
    protected final ExecutionManager executionManager = ExecutionManager.getInstance();

    private final AtomicBoolean isChanged = new AtomicBoolean(false);
    private final AtomicBoolean saving = new AtomicBoolean(false);

    protected void markChanged() {
        isChanged.lazySet(true);
    }

    protected void load() {
        saveBasedStorageReader.readTo(this);
    }

    public void save() {
        if (canSave()) {
            forceSave();
        }
    }

    private boolean canSave() {
        return isChanged.get() && !saving.get();
    }

    protected synchronized void forceSave() {
        saving.set(true);
        isChanged.set(false);
        executionManager.run(() -> {
            saveBasedStorageWriter.write(this);
            saving.set(false);
        });
    }
}
