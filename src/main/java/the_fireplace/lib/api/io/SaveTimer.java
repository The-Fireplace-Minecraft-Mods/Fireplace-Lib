package the_fireplace.lib.api.io;

import io.netty.util.internal.ConcurrentSet;
import the_fireplace.lib.api.multithreading.ConcurrentExecutionManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class SaveTimer {
    private static final Map<Short, Set<Runnable>> SAVE_INTERVAL_FUNCTIONS = new ConcurrentHashMap<>();
    private static Timer timer = new Timer();

    public static void registerSaveFunction(short saveIntervalInMinutes, Runnable... saveRunnables) {
        if (saveIntervalInMinutes < 1) {
            throw new IllegalArgumentException("Save interval must be at least one minute!");
        }
        Collections.addAll(
            SAVE_INTERVAL_FUNCTIONS.computeIfAbsent(saveIntervalInMinutes, newSaveIntervalInMinutes -> {
                addIntervalToTimer(newSaveIntervalInMinutes);
                return new ConcurrentSet<>();
            }),
            saveRunnables
        );
    }

    private static void addIntervalToTimer(short newSaveIntervalInMinutes) {
        int saveIntervalInMilliseconds = 1000 * 60 * newSaveIntervalInMinutes;
        int randomOffset = 200 + new Random().nextInt(59800);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Runnable runnable: SAVE_INTERVAL_FUNCTIONS.get(newSaveIntervalInMinutes)) {
                    ConcurrentExecutionManager.run(runnable);
                }
            }
        }, saveIntervalInMilliseconds - randomOffset, saveIntervalInMilliseconds);
    }

    public static void prepareForServerShutdown() {
        timer.cancel();
        saveAll();
    }

    public static void resetTimer() {
        timer = new Timer();
    }

    private static void saveAll() {
        for (Set<Runnable> intervalRunnables: SAVE_INTERVAL_FUNCTIONS.values()) {
            for (Runnable runnable: intervalRunnables) {
                ConcurrentExecutionManager.run(runnable);
            }
        }
    }
}
