package dev.the_fireplace.lib.impl.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.injectables.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.io.injectables.SaveBasedStorageWriter;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveDataStateManager;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveTimer;
import dev.the_fireplace.lib.api.lazyio.interfaces.Defaultable;
import dev.the_fireplace.lib.api.lazyio.interfaces.SaveData;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Implementation
@Singleton
public final class SaveDataStateManagerImpl implements SaveDataStateManager {

    private final SaveBasedStorageReader storageReader;
    private final SaveBasedStorageWriter storageWriter;
    private final SaveTimer saveTimer;
    private final ExecutionManager executionManager;
    private final ConcurrentMap<String, SaveDataState> dataStates = new ConcurrentHashMap<>(1);

    @Inject
    public SaveDataStateManagerImpl(
        SaveBasedStorageReader storageReader,
        SaveBasedStorageWriter storageWriter,
        SaveTimer saveTimer,
        ExecutionManager executionManager
    ) {
        this.storageReader = storageReader;
        this.storageWriter = storageWriter;
        this.saveTimer = saveTimer;
        this.executionManager = executionManager;
    }

    @Override
    public void initializeWithAutosave(SaveData saveData, short saveIntervalInMinutes) {
        if (saveIntervalInMinutes <= 0) {
            throw new IllegalArgumentException("Save interval must be greater than 0!");
        }
        dataStates.put(getStateKey(saveData), new SaveDataState(saveIntervalInMinutes));
        initialize(saveData);
        saveTimer.register(saveIntervalInMinutes, () -> save(saveData));
    }

    @Override
    public void initializeWithoutAutosave(SaveData saveData) {
        dataStates.put(getStateKey(saveData), new SaveDataState((short)0));
        initialize(saveData);
        if (isNonDefault(saveData)) {
            forceSave(saveData);
        }
    }

    private void initialize(SaveData saveData) {
        load(saveData);
        if (isNotDefaultable(saveData) || !storageReader.isStored(saveData)) {
            markChanged(saveData);
        }
    }

    @Override
    public void markChanged(SaveData saveData) {
        getState(saveData).isChanged.lazySet(true);
    }

    @Override
    public void load(SaveData saveData) {
        storageReader.readTo(saveData);
    }

    @Override
    public void save(SaveData saveData) {
        if (canSave(saveData)) {
            forceSave(saveData);
        }
    }

    private boolean canSave(SaveData saveData) {
        SaveDataState state = getState(saveData);
        return state.isChanged.get() && !state.saving.get();
    }

    @Override
    public void forceSave(SaveData saveData) {
        SaveDataState state = getState(saveData);
        state.saving.set(true);
        state.isChanged.set(false);
        executionManager.run(() -> {
            storageWriter.write(saveData);
            state.saving.set(false);
        });
    }

    @Override
    public void tearDown(SaveData saveData) {
        String key = getStateKey(saveData);
        if (dataStates.containsKey(key)) {
            SaveDataState state = dataStates.get(key);
            if (state.autosaveInterval > 0) {
                saveTimer.unregister(state.autosaveInterval);
            }
            dataStates.remove(key);
        }
    }

    private SaveDataState getState(SaveData saveData) {
        SaveDataState state = dataStates.get(getStateKey(saveData));

        if (state == null) {
            throw new IllegalStateException("Save data state not found!");
        }

        return state;
    }

    private static String getStateKey(SaveData saveData) {
        return saveData.getDatabase() + "-" + saveData.getTable() + "-" + saveData.getId();
    }

    private static boolean isNonDefault(Object obj) {
        return isNotDefaultable(obj) || !((Defaultable) obj).isDefault();
    }

    private static boolean isNotDefaultable(Object obj) {
        return !(obj instanceof Defaultable);
    }
}
