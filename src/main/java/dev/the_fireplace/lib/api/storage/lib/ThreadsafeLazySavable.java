package dev.the_fireplace.lib.api.storage.lib;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.api.storage.injectables.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.injectables.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.interfaces.SaveBasedSerializable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("FieldCanBeLocal")
@ThreadSafe
public abstract class ThreadsafeLazySavable implements SaveBasedSerializable {
    private final SaveBasedStorageReader saveBasedStorageReader = DIContainer.get().getInstance(SaveBasedStorageReader.class);
    private final SaveBasedStorageWriter saveBasedStorageWriter = DIContainer.get().getInstance(SaveBasedStorageWriter.class);

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
        DIContainer.get().getInstance(ExecutionManager.class).run(() -> {
            saveBasedStorageWriter.write(this);
            saving.set(false);
        });
    }
}
