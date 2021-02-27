package dev.the_fireplace.lib.impl.storage.utility;

import com.google.common.collect.Sets;
import dev.the_fireplace.lib.api.multithreading.ExecutionManager;
import dev.the_fireplace.lib.api.storage.utility.SaveTimer;
import dev.the_fireplace.lib.impl.FireplaceLib;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class SaveTimerImpl implements SaveTimer {
    @Deprecated
    public static final SaveTimer INSTANCE = new SaveTimerImpl();

    private final Map<Short, Set<Runnable>> saveIntervalFunctions = new ConcurrentHashMap<>();
    private Timer timer = new Timer();

    private SaveTimerImpl(){}

    @Override
    public void registerSaveFunction(short saveIntervalInMinutes, Runnable... saveRunnables) {
        register(saveIntervalInMinutes, saveRunnables);
    }

    @Override
    public void register(short saveIntervalInMinutes, Runnable... saveRunnables) {
        if (saveIntervalInMinutes < 1) {
            throw new IllegalArgumentException("Save interval must be at least one minute!");
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
                    ExecutionManager.getInstance().run(runnable);
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

        saveIntervalFunctions.get(saveIntervalInMinutes).removeAll(Sets.newHashSet(saveRunnables));
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
                ExecutionManager.getInstance().run(runnable);
            }
        }
    }
}
