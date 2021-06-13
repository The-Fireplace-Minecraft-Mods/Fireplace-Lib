package dev.the_fireplace.lib.impl.multithreading;

import dev.the_fireplace.annotateddi.di.Implementation;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.impl.FireplaceLib;
import dev.the_fireplace.lib.impl.config.FLConfig;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
@Implementation
public final class ConcurrentExecutionManager implements ExecutionManager {
    //Limit the number of active threads so we don't run the machine out of memory
    private ExecutorService essentialExecutorService = Executors.newFixedThreadPool(FLConfig.getData().getEssentialThreadPoolSize());
    private ExecutorService nonessentialExecutorService = Executors.newFixedThreadPool(FLConfig.getData().getNonEssentialThreadPoolSize());

    public ConcurrentExecutionManager() {}

    @Override
    public void run(Runnable runnable) {
        if (!essentialExecutorService.isShutdown()) {
            essentialExecutorService.execute(runnable);
        } else {
            FireplaceLib.getLogger().trace("Running essential runnable immediately because the executor has already stopped.", new Exception("Stack trace"));
            // We'll usually reach this if essential threads are finishing up and try to create more essential threads in the process.
            runnable.run();
        }
    }

    @Override
    public void runKillable(Runnable runnable) {
        if (!nonessentialExecutorService.isShutdown()) {
            nonessentialExecutorService.execute(runnable);
        } else {
            FireplaceLib.getLogger().debug("Failed to add nonessential runnable!", new Exception("Stack trace"));
        }
    }

    @Override
    public void waitForCompletion() throws InterruptedException {
        essentialExecutorService.shutdown();
        nonessentialExecutorService.shutdownNow();
        boolean timedOut = !essentialExecutorService.awaitTermination(1, TimeUnit.DAYS);
        if (timedOut) {
            FireplaceLib.getLogger().error("Timed out awaiting essential threads to terminate.");
        }
    }

    @Override
    public void startExecutors() {
        if (essentialExecutorService.isShutdown() || nonessentialExecutorService.isShutdown()) {
            try {
                waitForCompletion();
            } catch (InterruptedException e) {
                FireplaceLib.getLogger().error("Interrupted while waiting to complete essential execution!", e);
            }
            essentialExecutorService = Executors.newFixedThreadPool(FLConfig.getData().getEssentialThreadPoolSize());
            nonessentialExecutorService = Executors.newFixedThreadPool(FLConfig.getData().getNonEssentialThreadPoolSize());
        }
    }
}
