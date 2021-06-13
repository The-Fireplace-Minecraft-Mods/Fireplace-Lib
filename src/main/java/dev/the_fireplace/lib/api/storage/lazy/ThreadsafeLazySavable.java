package dev.the_fireplace.lib.api.storage.lazy;

import dev.the_fireplace.annotateddi.AnnotatedDI;
import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.storage.SaveBasedSerializable;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("FieldCanBeLocal")
@ThreadSafe
public abstract class ThreadsafeLazySavable implements SaveBasedSerializable {
    private final SaveBasedStorageReader saveBasedStorageReader = AnnotatedDI.getInjector().getInstance(SaveBasedStorageReader.class);
    private final SaveBasedStorageWriter saveBasedStorageWriter = AnnotatedDI.getInjector().getInstance(SaveBasedStorageWriter.class);

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
        AnnotatedDI.getInjector().getInstance(ExecutionManager.class).run(() -> {
            saveBasedStorageWriter.write(this);
            saving.set(false);
        });
    }
}
