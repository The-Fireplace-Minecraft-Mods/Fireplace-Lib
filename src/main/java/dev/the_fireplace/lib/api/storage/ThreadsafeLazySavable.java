package dev.the_fireplace.lib.api.storage;

import dev.the_fireplace.lib.api.io.SaveTimer;
import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.storage.access.StorageReader;
import dev.the_fireplace.lib.api.storage.access.StorageWriter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("FieldCanBeLocal")
@ThreadSafe
public abstract class ThreadsafeLazySavable implements Readable, Writable {
    private final SaveTimer timer = SaveTimer.getInstance();
    private final StorageReader storageReader = StorageReader.getInstance();
    private final StorageWriter storageWriter = StorageWriter.getInstance();
    @SuppressWarnings("WeakerAccess")
    protected final ExecutionManager executionManager = ExecutionManager.getInstance();

    private final AtomicBoolean isChanged = new AtomicBoolean(false);
    private final AtomicBoolean saving = new AtomicBoolean(false);

    protected ThreadsafeLazySavable(int saveIntervalInMinutes) {
        this((short)Math.min(saveIntervalInMinutes, Short.MAX_VALUE));
    }

    protected ThreadsafeLazySavable(short saveIntervalInMinutes) {
        storageReader.readTo(this);
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
            storageWriter.write(this);
            saving.set(false);
        });
    }
}
