package dev.the_fireplace.lib.lazyio;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SaveDataState
{
    public final short autosaveInterval;
    public final AtomicBoolean isChanged = new AtomicBoolean(false);
    public final AtomicBoolean saving = new AtomicBoolean(false);
    @Nullable
    public final Runnable autosaveRunnable;

    public SaveDataState(short autosaveInterval, @Nullable Runnable autosaveRunnable) {
        this.autosaveInterval = autosaveInterval;
        this.autosaveRunnable = autosaveRunnable;
    }
}
