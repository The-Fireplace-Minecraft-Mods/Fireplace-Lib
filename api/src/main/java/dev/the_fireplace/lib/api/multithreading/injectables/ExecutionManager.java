package dev.the_fireplace.lib.api.multithreading.injectables;

public interface ExecutionManager
{
    void run(Runnable runnable);

    void runKillable(Runnable runnable);

    void waitForCompletion() throws InterruptedException;

    void startExecutors();
}
