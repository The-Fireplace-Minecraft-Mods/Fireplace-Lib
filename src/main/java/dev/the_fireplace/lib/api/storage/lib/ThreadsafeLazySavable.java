package dev.the_fireplace.lib.api.storage.lib;

import dev.the_fireplace.annotateddi.AnnotatedDI;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.api.storage.injectables.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.injectables.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.interfaces.SaveBasedSerializable;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

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
