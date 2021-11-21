package dev.the_fireplace.lib.lazyio;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.lazyio.injectables.SaveTimer;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import io.netty.util.internal.ConcurrentSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Implementation
@Singleton
public final class SaveTimerImpl implements SaveTimer {
    private final Map<Short, Set<Runnable>> saveIntervalFunctions = new ConcurrentHashMap<>();
    private Timer timer = new Timer();
    private final ExecutionManager executionManager;

    @Inject
    public SaveTimerImpl(ExecutionManager executionManager) {
        this.executionManager = executionManager;
    }

    @Override
    public void register(short saveIntervalInMinutes, Runnable... saveRunnables) {
        if (saveIntervalInMinutes < 1) {
            throw new IllegalArgumentException("Save interval must be at least one minute!");
        }
        if (saveRunnables.length == 0) {
            throw new IllegalArgumentException("Must be registering at least one save runnable!");
        }
        Collections.addAll(
            saveIntervalFunctions.computeIfAbsent(saveIntervalInMinutes, newSaveIntervalInMinutes -> {
                addIntervalToTimer(newSaveIntervalInMinutes);
                return new ConcurrentSet<>();
            }),
            saveRunnables
        );
    }

    private void addIntervalToTimer(short newSaveIntervalInMinutes) {
        int saveIntervalInMilliseconds = 1000 * 60 * newSaveIntervalInMinutes;
        int randomOffset = 200 + new Random().nextInt(59800);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Runnable runnable: saveIntervalFunctions.get(newSaveIntervalInMinutes)) {
                    executionManager.run(runnable);
                }
            }
        }, saveIntervalInMilliseconds - randomOffset, saveIntervalInMilliseconds);
    }

    @Override
    public void unregister(short saveIntervalInMinutes, Runnable... saveRunnables) {
        if (!saveIntervalFunctions.containsKey(saveIntervalInMinutes)) {
            FireplaceLib.getLogger().warn("Attempted to remove save runnables from invalid time interval.", new Exception("Stack Trace"));
            return;
        }
        if (saveRunnables.length == 0) {
            throw new IllegalArgumentException("Must be unregistering at least one save runnable!");
        }

        saveIntervalFunctions.get(saveIntervalInMinutes).removeAll(Set.of(saveRunnables));
    }

    @Override
    public void prepareForServerShutdown() {
        timer.cancel();
        saveAll();
    }

    @Override
    public void resetTimer() {
        timer = new Timer();
    }

    private void saveAll() {
        for (Set<Runnable> intervalRunnables: saveIntervalFunctions.values()) {
            for (Runnable runnable: intervalRunnables) {
                executionManager.run(runnable);
            }
        }
    }
}
