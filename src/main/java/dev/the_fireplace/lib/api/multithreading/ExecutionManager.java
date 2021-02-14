package dev.the_fireplace.lib.api.multithreading;

import dev.the_fireplace.lib.impl.multithreading.ConcurrentExecutionManager;

public interface ExecutionManager {
    static ExecutionManager getInstance() {
        //noinspection deprecation
        return ConcurrentExecutionManager.INSTANCE;
    }

    void run(Runnable runnable);

    void runKillable(Runnable runnable);

    void waitForCompletion() throws InterruptedException;

    void startExecutors();
}
