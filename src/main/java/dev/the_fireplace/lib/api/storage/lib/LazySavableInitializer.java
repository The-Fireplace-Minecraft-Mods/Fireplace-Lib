package dev.the_fireplace.lib.api.storage.lib;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.storage.injectables.SaveBasedStorageReader;
import dev.the_fireplace.lib.api.storage.injectables.SaveTimer;
import dev.the_fireplace.lib.api.storage.interfaces.Defaultable;

public final class LazySavableInitializer {
    private static final SaveTimer TIMER = DIContainer.get().getInstance(SaveTimer.class);
    private static final SaveBasedStorageReader STORAGE_READER = DIContainer.get().getInstance(SaveBasedStorageReader.class);

    public static <T extends ThreadsafeLazySavable> T lazyInitialize(T savable, int saveIntervalInMinutes) {
        savable.load();
        if (isNotDefaultable(savable) && !STORAGE_READER.isStored(savable)) {
            savable.markChanged();
        }
        if (saveIntervalInMinutes > 0) {
            TIMER.register(boundToShort(saveIntervalInMinutes), savable::save);
        } else if (isNonDefault(savable)) {
            savable.forceSave();
        }

        return savable;
    }

    public static void tearDown(ThreadsafeLazySavable savable, int saveIntervalInMinutes) {
        TIMER.unregister(boundToShort(saveIntervalInMinutes), savable::save);
    }

    private static short boundToShort(int saveIntervalInMinutes) {
        return (short)Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, saveIntervalInMinutes));
    }

    private static boolean isNonDefault(Object obj) {
        return isNotDefaultable(obj) || !((Defaultable) obj).isDefault();
    }

    private static boolean isNotDefaultable(Object obj) {
        return !(obj instanceof Defaultable);
    }
}
