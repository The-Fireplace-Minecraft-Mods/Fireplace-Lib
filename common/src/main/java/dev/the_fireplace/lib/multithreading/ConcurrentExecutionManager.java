package dev.the_fireplace.lib.multithreading;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.multithreading.injectables.ExecutionManager;
import dev.the_fireplace.lib.domain.config.ConfigValues;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

@Singleton
@Implementation
public final class ConcurrentExecutionManager implements ExecutionManager
{
    private final ConfigValues configValues;
    private final Logger logger;
    private ExecutorService essentialExecutorService;
    private ExecutorService nonessentialExecutorService;

    @Inject
    public ConcurrentExecutionManager(ConfigValues configValues) {
        this.logger = FireplaceLibConstants.getLogger();
        this.configValues = configValues;
        essentialExecutorService = Executors.newFixedThreadPool(configValues.getEssentialThreadPoolSize());
        nonessentialExecutorService = Executors.newFixedThreadPool(configValues.getNonEssentialThreadPoolSize());
    }

    @Override
    public void run(Runnable runnable) {
        if (!essentialExecutorService.isShutdown()) {
            try {
                essentialExecutorService.execute(runnable);
            } catch (RejectedExecutionException exception) {
                logger.debug("Running essential runnable immediately because the executor rejected it.", exception);
                runnable.run();
            }
        } else {
            logger.trace("Running essential runnable immediately because the executor has already stopped.", new Exception("Stack trace"));
            // We'll usually reach this if essential threads are finishing up and try to create more essential threads in the process.
            runnable.run();
        }
    }

    @Override
    public void runKillable(Runnable runnable) {
        if (!nonessentialExecutorService.isShutdown()) {
            try {
                nonessentialExecutorService.execute(runnable);
            } catch (RejectedExecutionException exception) {
                logger.debug("Failed to add nonessential runnable because the executor rejected it.", exception);
            }
        } else {
            logger.trace("Failed to add nonessential runnable because the executor has already stopped.", new Exception("Stack trace"));
        }
    }

    @Override
    public void waitForCompletion() throws InterruptedException {
        essentialExecutorService.shutdown();
        nonessentialExecutorService.shutdownNow();
        boolean timedOut = !essentialExecutorService.awaitTermination(1, TimeUnit.DAYS);
        if (timedOut) {
            FireplaceLibConstants.getLogger().error("Timed out awaiting essential threads to terminate.");
        }
    }

    @Override
    public void startExecutors() {
        if (essentialExecutorService.isShutdown() || nonessentialExecutorService.isShutdown()) {
            try {
                waitForCompletion();
            } catch (InterruptedException e) {
                FireplaceLibConstants.getLogger().error("Interrupted while waiting to complete essential execution!", e);
            }
            essentialExecutorService = Executors.newFixedThreadPool(configValues.getEssentialThreadPoolSize());
            nonessentialExecutorService = Executors.newFixedThreadPool(configValues.getNonEssentialThreadPoolSize());
        }
    }
}
