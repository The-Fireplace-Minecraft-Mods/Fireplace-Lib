package dev.the_fireplace.lib.impl.lazyio;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SaveDataState {
    public final short autosaveInterval;
    public final AtomicBoolean isChanged = new AtomicBoolean(false);
    public final AtomicBoolean saving = new AtomicBoolean(false);

    public SaveDataState(short autosaveInterval) {
        this.autosaveInterval = autosaveInterval;
    }
}
