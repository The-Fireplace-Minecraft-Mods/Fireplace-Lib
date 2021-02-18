package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.access.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.storage.utility.SaveTimer;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("FieldCanBeLocal")
@ThreadSafe
public abstract class ThreadsafeLazySavable implements SaveBasedSerializable {
    private final SaveTimer timer = SaveTimer.getInstance();
    private final SaveBasedStorageReader saveBasedStorageReader = SaveBasedStorageReader.getInstance();
    private final SaveBasedStorageWriter saveBasedStorageWriter = SaveBasedStorageWriter.getInstance();
    @SuppressWarnings("WeakerAccess")
    protected final ExecutionManager executionManager = ExecutionManager.getInstance();

    private final AtomicBoolean isChanged = new AtomicBoolean(false);
    private final AtomicBoolean saving = new AtomicBoolean(false);

    protected ThreadsafeLazySavable(int saveIntervalInMinutes) {
        this((short)Math.min(saveIntervalInMinutes, Short.MAX_VALUE));
    }

    protected ThreadsafeLazySavable(short saveIntervalInMinutes) {
        load();
        if (saveIntervalInMinutes > 0) {
            timer.registerSaveFunction(saveIntervalInMinutes, this::save);
        } else if (isNonDefault()) {
            forceSave();
        }
    }

    private boolean isNonDefault() {
        return !(this instanceof Defaultable) || !((Defaultable) this).isDefault();
    }

    protected void markChanged() {
        isChanged.lazySet(true);
    }

    protected void load() {
        saveBasedStorageReader.readTo(this);
    }

    protected void save() {
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
