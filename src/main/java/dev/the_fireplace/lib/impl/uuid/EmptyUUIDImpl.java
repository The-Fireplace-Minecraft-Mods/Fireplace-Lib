package dev.the_fireplace.lib.impl.uuid;

import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;

import java.util.UUID;

public final class EmptyUUIDImpl implements EmptyUUID {
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    @Override
    public UUID get() {
        return EMPTY_UUID;
    }

    @Override
    public boolean is(UUID uuid) {
        return EMPTY_UUID.equals(uuid);
    }
}
